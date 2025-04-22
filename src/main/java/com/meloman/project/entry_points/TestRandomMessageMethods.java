package com.meloman.project.entry_points;

import com.meloman.project.communications.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class tests the random message sending functionality of the Server class.
 * It starts a test server and a mock client to verify the reception of random albums
 * and playlists. The test includes a retry mechanism for sending requests and uses
 * concurrency to handle multiple client connections simultaneously.
 */
public class TestRandomMessageMethods {
    private static final int CLIENT_PORT = 10800;
    private static final int MAX_RETRY_ATTEMPTS = 5;
    private static final int RETRY_DELAY_MS = 500;

    /**
     * Main entry point for the test application. Starts the server, initializes a mock client,
     * sends requests with retry logic, and waits for responses.
     *
     * @param args Command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        // Start the test server
        Server server = new Server();
        System.out.println("Server started");

        // Set up a mock client to receive messages
        final CountDownLatch messageLatch = new CountDownLatch(2); // Wait for both albums and playlists

        // Start client in a separate thread
        Thread clientThread = new Thread(() -> {
            setupTestClient(messageLatch);
        });
        clientThread.start();

        // Wait a moment to allow the server socket to initialize
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Sleep interrupted: " + e.getMessage());
        }

        // Send requests with retry mechanism
        System.out.println("Sending random albums request...");
        boolean albumsSuccess = sendWithRetry(() -> server.sendRandomAlbums(CLIENT_PORT));

        if (albumsSuccess) {
            System.out.println("Sending random playlists request...");
            boolean playlistsSuccess = sendWithRetry(() -> server.sendRandomPlaylists(CLIENT_PORT));

            if (!playlistsSuccess) {
                System.err.println("Failed to send random playlists after multiple attempts");
            }
        } else {
            System.err.println("Failed to send random albums after multiple attempts");
        }

        // Wait for responses (with timeout)
        try {
            boolean receivedAll = messageLatch.await(10, TimeUnit.SECONDS);
            if (receivedAll) {
                System.out.println("Test completed successfully!");
            } else {
                System.out.println("Test timed out waiting for responses");
            }
        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
        }
    }

    /**
     * Executes an operation with retry logic and incremental backoff delays.
     *
     * @param operation The operation to execute and potentially retry
     * @return true if the operation succeeded within the retry limits, false otherwise
     */
    private static boolean sendWithRetry(Runnable operation) {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                if (attempt > 0) {
                    System.out.println("Retry attempt " + (attempt + 1) + "...");
                    Thread.sleep(RETRY_DELAY_MS * attempt); // Increasing backoff
                }
                operation.run();
                return true; // Success
            } catch (Exception e) {
                System.err.println("Attempt " + (attempt + 1) + " failed: " + e.getMessage());
                if (attempt == MAX_RETRY_ATTEMPTS - 1) {
                    // Last attempt failed
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Configures a test client to listen for incoming messages on a specific port.
     * Creates separate threads to handle album and playlist responses.
     *
     * @param latch The countdown latch used to track received messages
     */
    private static void setupTestClient(final CountDownLatch latch) {
        try (ServerSocket serverSocket = new ServerSocket(CLIENT_PORT)) {
            System.out.println("Test client listening on port " + CLIENT_PORT);

            // Accept first connection (albums)
            Socket albumSocket = serverSocket.accept();
            new Thread(() -> handleClientConnection(albumSocket, "ALBUMS", latch)).start();

            // Accept second connection (playlists)
            Socket playlistSocket = serverSocket.accept();
            new Thread(() -> handleClientConnection(playlistSocket, "PLAYLISTS", latch)).start();

        } catch (Exception e) {
            System.err.println("Error in test client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles an individual client connection by reading the response and decrementing the latch.
     *
     * @param socket The client socket connection
     * @param type The type of message expected (used for logging purposes)
     * @param latch The countdown latch to decrement when message is received
     */
    private static void handleClientConnection(Socket socket, String type, CountDownLatch latch) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String response = in.readLine();
            System.out.println("Received " + type + " response: " + response);
            latch.countDown();
        } catch (Exception e) {
            System.err.println("Error handling " + type + " connection: " + e.getMessage());
        }
    }
}