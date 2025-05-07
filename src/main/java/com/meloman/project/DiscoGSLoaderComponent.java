package com.meloman.project;

import com.meloman.project.database_model.*;
import com.meloman.project.repositories.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Component for importing disco data from CSV into Derby database
 */
@Component
public class DiscoGSLoaderComponent {

    private static final String CSV_FILE_PATH = "src/main/resources/DiscoGSdata.cleaned.csv";

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

    /**
     * Import data from CSV file
     */
    public void importDataFromCsv() {
        // Maps to track entities that have been created
        Map<String, Artist> artistMap = new HashMap<>();
        Map<String, Label> labelMap = new HashMap<>();
        Map<String, Genre> genreMap = new HashMap<>();
        Map<String, Style> styleMap = new HashMap<>();
        Map<String, Album> albumMap = new HashMap<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_FILE_PATH, StandardCharsets.UTF_8))
                .withSkipLines(1) // Skip header
                .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    if (line.length < 6) {
                        System.out.println("Skipping invalid row: insufficient columns");
                        continue;
                    }

                    String trackName = line[0].trim();
                    String artistName = line[1].trim();
                    String albumName = line[2].trim();
                    String labelName = line[3].trim();
                    String genreName = line[4].trim();
                    String styleName = line[5].trim();
                    String durationStr = line.length > 6 ? line[6].trim() : "0:00";

                    double duration = 0.0;
                    try {
                        String[] parts = durationStr.split(":");
                        int minutes = Integer.parseInt(parts[0]);
                        int seconds = Integer.parseInt(parts[1]);
                        duration = minutes + seconds / 60.0;
                    } catch (Exception e) {
                        System.out.println("Invalid duration format for: " + durationStr + ", defaulting to 0.0");
                    }

                    System.out.println("Processing row: " + trackName + ", " + artistName + ", " +
                            albumName + ", " + labelName + ", " + genreName + ", " + styleName + ", " + durationStr);

                    if (trackName.isEmpty() || artistName.isEmpty()) {
                        System.out.println("Skipping row with missing track or artist name");
                        continue;
                    }

                    Artist artist = artistMap.get(artistName);
                    if (artist == null) {
                        String artistId = generateId("A", artistName);
                        artist = new Artist(artistId, artistName, "", "", "");
                        artistRepository.save(artist);
                        artistMap.put(artistName, artist);
                        System.out.println("Created artist: " + artistName);
                    }

                    Label label = labelMap.get(labelName);
                    if (label == null && !labelName.isEmpty()) {
                        String labelId = generateId("L", labelName);
                        label = new Label(labelId, labelName, "", "");
                        labelRepository.save(label);
                        labelMap.put(labelName, label);
                        System.out.println("Created label: " + labelName);
                    }

                    Genre genre = genreMap.get(genreName);
                    if (genre == null && !genreName.isEmpty()) {
                        String genreId = generateId("G", genreName);
                        genre = new Genre(genreId, genreName);
                        genreRepository.save(genre);
                        genreMap.put(genreName, genre);
                        System.out.println("Created genre: " + genreName);
                    }

                    Style style = styleMap.get(styleName);
                    if (style == null && !styleName.isEmpty()) {
                        String styleId = generateId("S", styleName);
                        style = new Style(styleId, styleName);
                        styleRepository.save(style);
                        styleMap.put(styleName, style);
                        System.out.println("Created style: " + styleName);
                    }

                    String albumKey = albumName + "-" + artistName;
                    Album album = albumMap.get(albumKey);
                    if (album == null && !albumName.isEmpty() && label != null) {
                        String albumId = generateId("AL", albumKey);
                        album = new Album(albumId, albumName, 0, "", "", new HashSet<>(), new HashSet<>(), artist, label);
                        albumRepository.save(album);
                        if (genre != null) {
                            album.getGenres().add(genre);
                        }
                        if (style != null) {
                            album.getStyles().add(style);
                        }
                        albumRepository.save(album);
                        albumMap.put(albumKey, album);
                        System.out.println("Created album: " + albumName);
                    }

                    if (label != null && artist != null) {
                        String trackId = generateId("T", trackName + "-" + artistName);
                        Set<Artist> trackArtists = new HashSet<>();
                        trackArtists.add(artist);

                        Track track = new Track(trackId, trackName, 0, new HashSet<>(), new HashSet<>(), trackArtists, label);
                        track.setDuration(duration);
                        trackRepository.save(track);

                        if (genre != null) {
                            track.getGenres().add(genre);
                        }

                        if (style != null) {
                            track.getStyles().add(style);
                        }

                        trackRepository.save(track);

                        System.out.println("Created track: " + trackName);
                    } else {
                        System.out.println("Skipping track due to missing label or artist: " + trackName);
                    }

                } catch (Exception e) {
                    System.err.println("Error processing row: " + e.getMessage());
                }
            }

            System.out.println("Import completed!");
            System.out.println("Stats:");
            System.out.println("Artists: " + artistMap.size());
            System.out.println("Labels: " + labelMap.size());
            System.out.println("Genres: " + genreMap.size() + " - " + genreMap.keySet());
            System.out.println("Styles: " + styleMap.size() + " - " + styleMap.keySet());
            System.out.println("Albums: " + albumMap.size());

        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Generate a simple ID with a prefix and hash of the name
     */
    private static String generateId(String prefix, String name) {
        int hash = Math.abs(name.hashCode()) % 100000;
        return prefix + hash;
    }
}