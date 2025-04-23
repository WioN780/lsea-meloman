package com.meloman.project.communications;

import com.meloman.project.service_model.User;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Client-side class handling user interactions and communication
 */

@Getter
@Setter

public class Client {
    private User currentUser;
    private String serverAddress;
    private int serverPort;
    private CountDownLatch responseLatch;
    private boolean lastRequestSuccessful;
    private String lastResponse;

    /**
     * Initializes connection to messaging server
     * @param serverAddress IP/hostname of server
     * @param port Server port number
     */
    public void connect(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.serverPort = port;
    }

    /**
     * Sends message to another user through server
     * @param recipientId Target user ID
     * @param messageContent Text content to send
     */
    public void sendMessage(String recipientId, String messageContent) {
        //TODO: Implement message packaging and sending
    }

    /**
     * Updates user's active status on server
     * @param isActive New status value
     */
    public void updateActivityStatus(boolean isActive) {
        //TODO: Implement status update transmission
    }

    /**
     * Fetches current user list from server
     * @return List of available users with basic info
     */
    public List<User> requestUserList() {
        //TODO: Implement user list request/response handling
        return null;
    }

    /**
     * Sets up a latch for testing
     * @param latch CountDownLatch for tracking responses
     */
    public void setResponseLatch(CountDownLatch latch) {
        this.responseLatch = latch;
    }

    /**
     * Requests random albums from server
     * @return true if request was successful, false otherwise
     */
    public boolean requestRandomAlbums() {
        return sendRequest(RequestType.GET_10_RANDOM_ALBUMS);
    }

    /**
     * Requests random playlists from server
     * @return true if request was successful, false otherwise
     */
    public boolean requestRandomPlaylists() {
        return sendRequest(RequestType.GET_10_RANDOM_PLAYLISTS);
    }

    /**
     * Sends a request to the server and handles the response
     * @param requestType The type of request to send
     * @return true if the request was successful, false otherwise
     */
    private boolean sendRequest(RequestType requestType) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Create and send request
            String requestString = requestType.toString();
            System.out.println("Sending request: " + requestString);
            out.println(requestString);

            // Wait for and process response
            String responseStr = in.readLine();
            System.out.println("Received response: " + responseStr);

            // Store response for inspection
            this.lastResponse = responseStr;

            // Check if response indicates success
            this.lastRequestSuccessful = responseStr != null && !responseStr.contains("false");

            // Signal that we received a response (for testing)
            if (responseLatch != null) {
                responseLatch.countDown();
            }

            return this.lastRequestSuccessful;

        } catch (IOException e) {
            System.err.println("Error sending request: " + e.getMessage());
            return false;
        }
    }
}