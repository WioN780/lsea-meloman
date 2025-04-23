package com.meloman.project.communications;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.utils.DiscoGSLoader;
import com.meloman.project.utils.SpotifyPlaylistLoader;
import com.meloman.project.service_model.User;


import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
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
    private RequestHandler requestHandler;

    private final static int portUDP = 10555;
    private final static int portTCP = 10556;

    public Server() {
        // Initialize collections
        this.connectedUsers = new ArrayList<>();
        this.availableAlbums = new ArrayList<>();
        this.availablePlaylists = new ArrayList<>();

        // Initialize request handler
        this.requestHandler = new RequestHandler(this);

        // Load data using utility classes
        loadData();

        // Start listeners
        startUDPListener();
        startTCPListener();

        System.out.println("Server initialized with " + availableAlbums.size() + " albums and "
                + availablePlaylists.size() + " playlists");
    }

    /**
     * Loads album and playlist data from respective loaders
     */
    private void loadData() {
        try {
            // Load albums with DiscoGSLoader (limit to 1000 for performance)
            DiscoGSLoader albumLoader = new DiscoGSLoader();
            InputStream albumStream = SpotifyPlaylistLoader.class.getClassLoader()
                    .getResourceAsStream("com/meloman/project/data/DiscoGSdata.csv");
            if (albumStream != null) {
                this.availableAlbums = albumLoader.loadAlbums(albumStream, 1000);
            } else {
                System.err.println("Could not find album data file");
            }

            // Load playlists with SpotifyPlaylistLoader
            SpotifyPlaylistLoader playlistLoader = new SpotifyPlaylistLoader();
            InputStream playlistStream = SpotifyPlaylistLoader.class.getClassLoader()
                    .getResourceAsStream("com/meloman/project/data/mpd.slice.0-999.json");
            if (playlistStream != null) {
                this.availablePlaylists = playlistLoader.loadPlaylists(playlistStream);
            } else {
                System.err.println("Could not find playlist data file");
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                                        Response response = handleRequest(request);

                                        // Send response back to client
                                        if (response != null) {
                                            out.println(response);
                                        }
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
     * @return Response object to be sent back to client
     */
    public Response handleRequest(Request request) {
        if (request == null) {
            return new Response(false, null, "Invalid request");
        }

        switch (request.getType()) {
            case GET_10_RANDOM_ALBUMS:
                return requestHandler.handleRandomAlbumsRequest();
            case GET_10_RANDOM_PLAYLISTS:
                return requestHandler.handleRandomPlaylistsRequest();
            // Handle other request types
            default:
                return new Response(false, null, "Unsupported request type: " + request.getType());
        }
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
     * Helper method to select n random items from a list
     * @param items Source list to select from
     * @param count Number of items to select
     * @return List of randomly selected items
     */
    public <T> List<T> getRandomItems(List<T> items, int count) {
        List<T> result = new ArrayList<>();
        int size = items.size();

        if (size <= 0) {
            return result; // Return empty list if no items
        }

        if (size <= count) {
            return new ArrayList<>(items); // Return all items if fewer than requested
        }

        // Create a list of indices and shuffle it
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        // Select the first 'count' items using the shuffled indices
        for (int i = 0; i < count; i++) {
            result.add(items.get(indices.get(i)));
        }

        return result;
    }

    public int getPortTCP() {
        return portTCP;
    }

    public int getPortUDP() {
        return portUDP;
    }
}