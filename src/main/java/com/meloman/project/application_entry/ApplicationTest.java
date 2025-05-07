package com.meloman.project.application_entry;

import com.meloman.project.DiscoGSLoaderComponent;
import com.meloman.project.SpotifyPlaylistLoaderComponent;
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
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1-4): ");

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