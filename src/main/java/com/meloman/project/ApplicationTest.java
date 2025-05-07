package com.meloman.project;

import com.meloman.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Scanner;

/**
 * Unified Spring Boot application that can run both loader services
 */
@SpringBootApplication(scanBasePackages = "com.meloman.project")
@EnableTransactionManagement
public class ApplicationTest {

    @Autowired
    private DiscoGSLoaderComponent discoGSLoaderComponent;

    @Autowired
    private SpotifyPlaylistLoaderComponent spotifyPlaylistLoaderComponent;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("Database Loader Test");
                System.out.println("1. Load Songs from DiscoGS CSV");
                System.out.println("2. Load Playlists from Spotify JSON");
                System.out.println("3. Load Both");
                System.out.println("4. Run tests");
                System.out.println("5. Exit");
                System.out.print("Enter your choice (1-5): ");

                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            loadSongs();
                            break;
                        case 2:
                            loadPlaylists();
                            break;
                        case 3:
                            loadSongs();
                            loadPlaylists();
                            break;
                        case 4:
                            runTests();
                            break;
                        case 5:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Clear invalid input
                }
            }

            System.exit(0);
        };
    }

    private void runTests() {

        // 1) DiscoGS import test
        //measure("DiscoGS CSV import", this::loadSongs);

        // 2) Spotify import test
        //measure("Spotify playlist import", this::loadPlaylists);

        // 3) Artists with the most tracks
        measure("findWithMostTracks()",
                () -> System.out.println(
                        "Artists returned: " + artistRepository.findTopArtists(10).size())
        );

        // 4) Top 10 styles by appearance
        final int N = 10;
        measure("findMostPopularStyles(" + N + ")",
                () -> System.out.println(
                        "Styles returned:  " + styleRepository.findMostPopular(N).size())
        );

        // 5) Top 10 genres by appearance
        measure("findMostPopularGenres(" + N + ")",
                () -> System.out.println(
                        "Genres returned:  " + genreRepository.findMostPopular(N).size())
        );

        // 6) Labels with the most tracks
        measure("findWithMostTracks() on LabelRepository",
                () -> System.out.println(
                        "Labels returned:  " + labelRepository.findTopLabels(10).size())
        );

        System.out.println("All tests complete.");
    }

    /**
     * Utility wrapper
     *
     * @param label description
     * @param task  code to execute
     */
    private void measure(String label, Runnable task) {
        Runtime rt = Runtime.getRuntime();

        rt.gc();
        long memBefore = rt.totalMemory() - rt.freeMemory();

        long start = System.nanoTime();

        task.run();

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        rt.gc();
        long memAfter = rt.totalMemory() - rt.freeMemory();

        long diffKb = (memAfter - memBefore) / 1024;

        System.out.printf("%-35s : %6d ms  |  %+8d KB%n", label, elapsedMs, diffKb);
    }

    /**
     * Loads songs from DiscoGS CSV file
     */
    private void loadSongs() {
        System.out.println("Starting DiscoGS CSV Import");
        discoGSLoaderComponent.importDataFromCsv();
        System.out.println("DiscoGS Import Complete");
    }

    /**
     * Loads playlists from Spotify JSON files
     */
    private void loadPlaylists() {
        System.out.println("Starting Spotify Playlist Import");

        // Get number of files to process
        Scanner scanner = new Scanner(System.in);
        int numFiles;
        while (true) {
            System.out.print("Enter the number of files to process (max is 1000): ");
            if (scanner.hasNextInt()) {
                numFiles = scanner.nextInt();
                if (numFiles > 0) break;
                System.out.println("Please enter a positive number.");
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next(); // Clear invalid input
            }
        }

        // Generate file paths
        List<String> filePaths = spotifyPlaylistLoaderComponent.generateFilePaths(numFiles);

        // Process files
        spotifyPlaylistLoaderComponent.printDatabaseStats();
        spotifyPlaylistLoaderComponent.processAndDisplayResults(filePaths);

        System.out.println("Spotify Import Complete");
    }
}