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
    private ArrayList<User> connectedUsers;
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
                    .getResourceAsStream("DiscoGSdata.cleaned.csv");
            if (albumStream != null) {
                this.availableAlbums = albumLoader.loadAlbums(albumStream, 1000);
            } else {
                System.err.println("Could not find album data file");
            }

            // Load playlists with SpotifyPlaylistLoader
            SpotifyPlaylistLoader playlistLoader = new SpotifyPlaylistLoader();
            InputStream playlistStream = SpotifyPlaylistLoader.class.getClassLoader()
                    .getResourceAsStream("spotify/mpd.slice.0-999.json");
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
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(portUDP)) {
                System.out.println("Listening on UDP port " + portUDP + " …");

                byte[] buffer = new byte[65535];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    // Deserialize the request
                    ByteArrayInputStream byteIn = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                    ObjectInputStream objIn = new ObjectInputStream(byteIn);
                    Request req = (Request) objIn.readObject();

                    // Handle the request
                    Response resp = requestHandler.handle(req);

                    // Serialize the response
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
                    objOut.writeObject(resp);
                    objOut.flush();
                    byte[] responseBytes = byteOut.toByteArray();

                    // Send the response
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseBytes, responseBytes.length,
                            packet.getAddress(), packet.getPort()
                    );
                    socket.send(responsePacket);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * Starts listening for incoming client connections (TCP)
     */
    public void startTCPListener() {

        new Thread(() -> {
            ExecutorService pool = null;

            try (ServerSocket server = new ServerSocket(portTCP)) {
                System.out.println("Listening on TCP port " + portTCP + " …");

                while (true) {
                    final Socket socket = server.accept();
                    handleClientTCP(socket);
                }

            } catch (IOException ioe) {
                System.err.println("Cannot open TCP port " + portTCP);
                ioe.printStackTrace();
            } finally {
                System.out.println("TCP listener stopped");
            }
        }).start();
    }

    private void handleClientTCP(Socket socket) {
        System.out.println(socket + "  -  connected");

        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())) {

            while (true) {
                Object o = in.readObject();
                if (!(o instanceof Request req)) break;

                Response resp = requestHandler.handle(req);
                out.writeObject(resp);
                out.flush();
            }
        } catch (EOFException eof) {
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignore) {}
            System.out.println(socket + "  ⇢  closed");
        }
    }

    /**
     * Helper method to select n random items from a list
     * @param items Source list to select from
     * @param count Number of items to select
     * @return List of randomly selected items
     */
    public <T> ArrayList<T> getRandomItems(List<T> items, int count) {
        ArrayList<T> result = new ArrayList<>();
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