package com.meloman.project.communications;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.utils.DiscoGSLoader;
import com.meloman.project.utils.SpotifyPlaylistLoader;
import com.meloman.project.service_model.User;


import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Server-side class managing user connections and message routing
 */

@Getter
@Setter
public class Server {
    private List<User> connectedUsers;
    private List<Album> availableAlbums;
    private List<Playlist> availablePlaylists;

    private final static int portUDP = 10555;
    private final static int portTCP = 10556;

    /**
     * Starts listening for incoming client connections (UDP)
     */
    public void startUDPListener() {
        // UDP
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (DatagramSocket socket = new DatagramSocket(portUDP)) {
                    byte[] buf = new byte[socket.getReceiveBufferSize()];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);

                    System.out.println("Listening on UDP port " + portUDP + ", Say hi!");
                    while (true) {
                        socket.receive(packet);
                        // TODO make up where to implement UDP
                    }
                } catch (IOException ioe) {
                    System.err.println("Cannot open the port on UDP");
                    ioe.printStackTrace();
                } finally {
                    System.out.println("Closing UDP server");
                }
            }
        }).start();
    }

    /**
     * Starts listening for incoming client connections (TCP)
     */
    public void startTCPListener() {
        // TCP
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = null;
                try (ServerSocket server = new ServerSocket(portTCP)) {
                    executor = Executors.newFixedThreadPool(5);
                    System.out.println("Listening on TCP port " + portTCP + ", Say hi!");
                    while (true) {
                        final Socket socket = server.accept();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                String inputLine = "";
                                System.err.println(
                                        socket.toString() + " ~> connected");
                                try (PrintWriter out = new PrintWriter(
                                        socket.getOutputStream(), true);
                                     BufferedReader in = new BufferedReader(
                                             new InputStreamReader(socket
                                                     .getInputStream()))) {
                                    while ((inputLine = in.readLine()) != null && !inputLine.equals("!quit")) {
                                        System.out.println(socket.toString() + ": " + inputLine);

                                        // Convert input to request
                                        Request request = Request.fromString(inputLine);

                                        // Handle the request
                                        handleRequest(request);

                                        // out.println("ACK: " + request.getType());
                                    }
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                } finally {
                                    try {
                                        System.err.println(socket.toString()
                                                + " ~> closing");
                                        socket.close();
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } catch (IOException ioe) {
                    System.err.println("Cannot open the port on TCP");
                    ioe.printStackTrace();
                } finally {
                    System.out.println("Closing TCP server");
                    if (executor != null) {
                        executor.shutdown();
                    }
                }
            }
        }).start();
    }

    /**
     * Handles incoming client requests and routes them to appropriate handlers
     * @param request Received request object
     */
    private void handleRequest(Request request) {
        //TODO: Implement request type distinction
        //Should call specific handlers based on request type
    }

    /**
     * Maintains list of currently connected users
     * @param user User to add/update in the list
     */
    public void updateUserStatus(User user) {
        //TODO: Add/update user in connectedUsers list
    }

    /**
     * Broadcasts message status updates to relevant clients
     * @param messageId ID of message to update
     * @param recipientId Target user for status update
     */
    public void broadcastMessageStatus(String messageId, String recipientId) {
        //TODO: Implement status propagation logic
    }

    /**
     * Sending a list of 10 random playlists
     * @param port of the receiver
     */
    public void sendRandomPlaylists(int port) {
        //TODO: Implement status propagation logic
    }

    /**
     * Sending a list of 10 random albums
     * @param port of the receiver
     */
    public void sendRandomAlbums(int port) {
        //TODO: Implement status propagation logic
    }
}