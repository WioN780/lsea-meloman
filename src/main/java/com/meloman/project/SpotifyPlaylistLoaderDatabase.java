package com.meloman.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meloman.project.database_model.*;
import com.meloman.project.repositories.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Spotify Import application for processing multiple JSON files and creating playlist database objects.
 */
@SpringBootApplication(scanBasePackages = "com.meloman.project")
@EnableTransactionManagement
public class SpotifyPlaylistLoaderDatabase {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyPlaylistLoaderDatabase.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            PlaylistRepository playlistRepository,
            TrackRepository trackRepository,
            ArtistRepository artistRepository) {

        return args -> {
            try {
                SpotifyImporter importer = new SpotifyImporter(playlistRepository, trackRepository, artistRepository);
                importer.printDatabaseStats();

                // Always get input from user
                int numFiles = readNumberOfFilesFromUser();
                List<String> filePaths = generateFilePaths(numFiles);
                System.out.println("Processing " + numFiles + " files:");
                filePaths.forEach(path -> System.out.println("- " + path));
                System.out.println();

                Map<String, List<String>> results = importer.processFiles(filePaths);
                importer.displayResults(results);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            System.exit(0);
        };
    }

    /**
     * Reads the number of files from user input with validation.
     */
    private static int readNumberOfFilesFromUser() {
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
        return numFiles;
    }

    /**
     * Generates file paths based on the number of files.
     * Format: mpd.slice.{start}-{end}.json where start = i*1000, end = start+999
     */
    private static List<String> generateFilePaths(int numFiles) {
        List<String> filePaths = new ArrayList<>();
        String basePath = "src/main/resources/spotify/";
        for (int i = 0; i < numFiles; i++) {
            int start = i * 1000;
            String fileName = String.format("mpd.slice.%d-%d.json", start, start + 999);
            filePaths.add(basePath + fileName);
        }
        return filePaths;
    }

    /**
     * Inner class to handle all the Spotify JSON parsing and playlist creation logic.
     * This class receives repositories via constructor injection to ensure they are properly initialized.
     */
    public static class SpotifyImporter {
        private final PlaylistRepository playlistRepository;
        private final TrackRepository trackRepository;
        private final ArtistRepository artistRepository;
        private final ObjectMapper objectMapper;

        public SpotifyImporter(
                PlaylistRepository playlistRepository,
                TrackRepository trackRepository,
                ArtistRepository artistRepository) {
            this.playlistRepository = playlistRepository;
            this.trackRepository = trackRepository;
            this.artistRepository = artistRepository;
            this.objectMapper = new ObjectMapper();
        }

        /**
         * Print current database statistics
         */
        public void printDatabaseStats() {
            System.out.println("Database status:");
            System.out.println("- Tracks: " + trackRepository.count());
            System.out.println("- Artists: " + artistRepository.count());
            System.out.println("- Playlists: " + playlistRepository.count());
            System.out.println();
        }

        /**
         * Process multiple files and return results
         *
         * @param filePaths List of file paths to process
         * @return Map with file paths as keys and lists of created playlist IDs as values
         */
        public Map<String, List<String>> processFiles(List<String> filePaths) {
            Map<String, List<String>> results = new HashMap<>();

            for (String filePath : filePaths) {
                System.out.println("Processing file: " + filePath);
                File jsonFile = new File(filePath);

                if (!jsonFile.exists()) {
                    System.err.println("Warning: File does not exist: " + filePath);
                    results.put(filePath, Collections.emptyList());
                    continue;
                }

                try {
                    List<String> createdPlaylists = parseSpotifyJson(jsonFile);
                    results.put(filePath, createdPlaylists);
                    System.out.println("Created " + createdPlaylists.size() + " playlists from " + filePath);
                } catch (IOException e) {
                    System.err.println("Error processing file " + filePath + ": " + e.getMessage());
                    results.put(filePath, Collections.emptyList());
                }
            }

            return results;
        }

        /**
         * Display the results of processing multiple files
         */
        public void displayResults(Map<String, List<String>> results) {
            int totalPlaylists = results.values().stream().mapToInt(List::size).sum();

            System.out.println("\nImport completed!");
            System.out.println("Created " + totalPlaylists + " playlists from " + results.size() + " files.");

            for (Map.Entry<String, List<String>> entry : results.entrySet()) {
                String filePath = entry.getKey();
                List<String> playlistIds = entry.getValue();

                System.out.println("\nFile: " + filePath);
                System.out.println("Created " + playlistIds.size() + " playlists.");

                if (!playlistIds.isEmpty()) {
                    System.out.println("Created playlist IDs:");
                    for (String id : playlistIds) {
                        System.out.println("- " + id);
                    }
                } else {
                    System.out.println("No playlists were created from this file.");
                }
            }
        }

        /**
         * Parse a Spotify JSON file and create playlists with matching tracks.
         *
         * @param jsonFile The Spotify JSON file to parse
         * @return List of created playlist IDs
         * @throws IOException If there is an error reading or parsing the file
         */
        @Transactional
        public List<String> parseSpotifyJson(File jsonFile) throws IOException {
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode playlistsNode = rootNode.get("playlists");

            List<String> createdPlaylists = new ArrayList<>();

            if (playlistsNode != null && playlistsNode.isArray()) {
                for (JsonNode playlistNode : playlistsNode) {
                    String playlistId = processPlaylist(playlistNode);
                    if (playlistId != null) {
                        createdPlaylists.add(playlistId);
                    }
                }
            }

            return createdPlaylists;
        }

        /**
         * Parse a Spotify JSON file by path and create playlists with matching tracks.
         *
         * @param filePath Path to the Spotify JSON file
         * @return List of created playlist IDs
         * @throws IOException If there is an error reading or parsing the file
         */
        @Transactional
        public List<String> parseSpotifyJson(String filePath) throws IOException {
            File jsonFile = new File(filePath);
            return parseSpotifyJson(jsonFile);
        }

        /**
         * Process a single playlist JSON node and create a playlist if matching tracks are found.
         *
         * @param playlistNode The playlist JSON node
         * @return The created playlist ID or null if no matching tracks found
         */
        @Transactional(propagation = Propagation.REQUIRED)
        protected String processPlaylist(JsonNode playlistNode) {
            String playlistName = playlistNode.get("name").asText();
            boolean collaborative = Boolean.parseBoolean(playlistNode.get("collaborative").asText());
            long modifiedAt = playlistNode.get("modified_at").asLong();
            int numFollowers = playlistNode.get("num_followers").asInt();
            JsonNode tracksNode = playlistNode.get("tracks");

            Set<Track> matchedTracks = new HashSet<>();

            if (tracksNode != null && tracksNode.isArray()) {
                for (JsonNode trackNode : tracksNode) {
                    Track matchedTrack = findMatchingTrack(trackNode);
                    if (matchedTrack != null) {
                        // Make sure we have a fully initialized entity
                        Track fullTrack = trackRepository.findByIdWithArtists(matchedTrack.getId()).orElse(null);
                        if (fullTrack != null) {
                            // Initialize the artists collection
                            Hibernate.initialize(fullTrack.getArtists());
                            matchedTracks.add(fullTrack);
                        }
                    }
                }
            }

            // Only create playlist if we found matching tracks
            if (!matchedTracks.isEmpty()) {
                String playlistId = UUID.randomUUID().toString();
                Playlist playlist = new Playlist(playlistId, playlistName, collaborative, modifiedAt, numFollowers, matchedTracks);
                playlistRepository.save(playlist);
                return playlistId;
            }

            return null;
        }

        /**
         * Find a matching track in the database based on track name and artist name.
         *
         * @param trackNode The track JSON node
         * @return The matching Track entity or null if no match found
         */
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        protected Track findMatchingTrack(JsonNode trackNode) {
            String trackName = trackNode.get("track_name").asText();
            String artistName = trackNode.get("artist_name").asText();

            // First try using a custom query if available
            try {
                // Assuming you have implemented this method in your repository
                List<Track> tracksWithArtists = trackRepository.findByTitleWithArtists(trackName);
                for (Track track : tracksWithArtists) {
                    if (track.getArtists().stream()
                            .anyMatch(artist -> artist.getName().equals(artistName))) {
                        return track;
                    }
                }
            } catch (Exception e) {
                // Fall back to manual approach if the custom query isn't implemented
                List<Track> matchingNameTracks = trackRepository.findByTitle(trackName);

                for (Track track : matchingNameTracks) {
                    // Use Hibernate.initialize to eagerly initialize the collection
                    Hibernate.initialize(track.getArtists());

                    if (track.getArtists().stream()
                            .anyMatch(artist -> artist.getName().equals(artistName))) {
                        return track;
                    }
                }
            }

            return null;
        }
    }
}