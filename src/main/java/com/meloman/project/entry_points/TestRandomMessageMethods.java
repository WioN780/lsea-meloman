package com.meloman.project.entry_points;

import com.meloman.project.communications.Client;
import com.meloman.project.communications.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class tests both the random message functionality using the Server's TCP listener
 * and the UDP communication capabilities.
 * It starts a server and a client that sends requests through both TCP and UDP ports.
 */
public class TestRandomMessageMethods {
    /**
     * Main entry point for the test application.
     *
     * @param args Command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        // Start the test server
        Server server = new Server();
        System.out.println("Server started on TCP port " + server.getPortTCP() + " and UDP port " + server.getPortUDP());

        // Set up a client for TCP communication
        final CountDownLatch messageLatch = new CountDownLatch(2); // Wait for both TCP responses

        Client client = new Client();
        client.connect("localhost", server.getPortTCP());
        client.setResponseLatch(messageLatch);

        // Wait a moment to allow the server sockets to initialize
        try {
            Thread.sleep(1000);
            System.out.println("Server initialization pause complete");
        } catch (InterruptedException e) {
            System.err.println("Sleep interrupted: " + e.getMessage());
        }

        // Send TCP requests
        System.out.println("Sending random albums request via TCP...");
        boolean albumsSuccess = client.requestRandomAlbums();
        System.out.println("TCP albums request " + (albumsSuccess ? "succeeded" : "failed"));

        if (albumsSuccess) {
            System.out.println("Sending random playlists request via TCP...");
            boolean playlistsSuccess = client.requestRandomPlaylists();
            System.out.println("TCP playlists request " + (playlistsSuccess ? "succeeded" : "failed"));
        }

        // Test UDP communication
        testUDPCommunication(server.getPortUDP());

        // Wait for TCP responses (with timeout)
        try {
            boolean receivedAll = messageLatch.await(10, TimeUnit.SECONDS);
            if (receivedAll) {
                System.out.println("TCP test completed successfully!");
            } else {
                System.out.println("TCP test timed out waiting for responses");
            }
        } catch (InterruptedException e) {
            System.err.println("TCP test interrupted: " + e.getMessage());
        }

        System.out.println("All tests completed");
    }

    /**
     * Tests the UDP communication with the server
     *
     * @param udpPort The UDP port on which the server is listening
     */
    private static void testUDPCommunication(int udpPort) {
        System.out.println("Starting UDP communication test...");
        System.err.print("UDP NOT IMPLEMENTED YET.");
        }
    }
