package com.meloman.project.entry_points;
import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.utils.DiscoGSLoader;
import com.meloman.project.utils.SpotifyPlaylistLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class ProcessingTest {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a test to run:");
        System.out.println("1: Test PlaylistJsonLoader");
        System.out.println("2: Test DiscoGSLoader");
        System.out.print("Enter your choice (1-2): ");

        int choice = 0;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.err.println("Invalid input. Please enter a number between 1 and 2.");
            return;
        }

        // Test PlaylistJsonLoader
        if (choice == 1) {
            for (int j = 0; j < 11; j++) {
                try {
                    SpotifyPlaylistLoader loader = new SpotifyPlaylistLoader();

                    String file = "com/meloman/project/data/mpd.slice." + 1000 * j + "-" + ((1000 * (j + 1)) - 1) + ".json";

                    InputStream inputStream = SpotifyPlaylistLoader.class.getClassLoader()
                            .getResourceAsStream(file);

                    if (inputStream == null) {
                        throw new IOException("File not found." + file);
                    }

                    List<Playlist> playlists = loader.loadPlaylists(inputStream);
                    System.out.println(playlists.size() + " playlists loaded");

                    // Print details of first few playlists if available
                    int playlistsToPrint = Math.min(3, playlists.size());
                    for (int i = 0; i < playlistsToPrint; i++) {
                        System.out.println("\nPlaylist " + (i + 1) + " details:");
                        System.out.println(playlists.get(i).toString());
                        System.out.println("Number of tracks: " + playlists.get(i).getNumTracks());
                        System.out.println("Number of albums: " + playlists.get(i).getNumAlbums());
                        System.out.println("Number of artists: " + playlists.get(i).getNumArtists());
                    }

                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }


        }
        // Test DiscoGSLoader
        else if (choice == 2) {
            try {
                DiscoGSLoader loader = new DiscoGSLoader();

                InputStream inputStream = SpotifyPlaylistLoader.class.getClassLoader()
                        .getResourceAsStream("com/meloman/project/data/DiscoGSdata.csv");

                if (inputStream == null) {
                    throw new IOException("File not found: DiscoGSdata.csv");
                }

                List<Album> albums = loader.loadAlbums(inputStream);
                System.out.println(albums.size() + " albums loaded");

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        else {
            System.out.println("Invalid choice. Please run again and select a number between 1 and 2.");
        }
        scanner.close();
    }
}