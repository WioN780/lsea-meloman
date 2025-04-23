package com.meloman.project.entry_points;

import com.meloman.project.communications.*;
import com.meloman.project.service_model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestRandomMessageMethods {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Start the test server
        Server server = new Server();

        System.out.println("Server started on TCP port " + server.getPortTCP() + " and UDP port " + server.getPortUDP());

        // Wait a moment to allow the server sockets to initialize
        try {
            Thread.sleep(1000);
            System.out.println("Server initialization pause complete");
        } catch (InterruptedException e) {
            System.err.println("Sleep interrupted: " + e.getMessage());
        }

        Client client = new Client(server.getPortTCP(), server.getPortUDP(), new User(UUID.randomUUID(), "user1", "e@12", "123"));

        // Send TCP requests
        System.out.println("Sending random albums request via TCP...");

        Request albumsRequest = new Request((UUID) client.getCurrentUser().getUserId(), RequestType.GET_10_RANDOM_ALBUMS);

        Response albumsResponse = client.sendRequestTCP(albumsRequest);
        System.out.println("TCP albums request " + (albumsResponse.isSuccess() ? "succeeded" : "failed"));

        if (albumsResponse.isSuccess()) {
            System.out.println(albumsResponse.getData().toString());
        }

        /** Test UDP communication
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
*/
        System.out.println("All tests completed");
    }
}
