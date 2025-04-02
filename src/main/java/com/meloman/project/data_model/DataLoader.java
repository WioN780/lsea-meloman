package com.meloman.project.data_model;

import java.io.*;
import java.util.*;

public class DataLoader {

    // Maps to avoid duplicate creations.
    private Map<String, Artist> artistMap = new HashMap<>();
    private Map<String, Label> labelMap = new HashMap<>();
    private Map<String, Album> albumMap = new HashMap<>();

    /**
     * Loads albums from the provided CSV InputStream.
     * @param inputStream the input stream of the CSV file.
     * @return list of Album objects.
     * @throws IOException if an I/O error occurs.
     */
    public List<Album> loadAlbums(InputStream inputStream) throws IOException {
        List<Album> albums = new ArrayList<>();
        int processedLines = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // Skip the header row.
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            processLine(line, albums);
            processedLines++;

            if (processedLines >= 1500000) { //Limit for now until further optimization.
                System.out.println(processedLines + " linea prozetatutak.");
                System.out.println(albums.get(albums.size() - 1).toString());
            }
        }
        reader.close();
        System.out.println("Albumes guztiak: " + albums.size());
        System.out.println("Linea guztiak: " + processedLines);
        return albums;
    }

    private void processLine(String line, List<Album> albums) {
        if (line == null || line.trim().isEmpty()) return;
        // Assuming CSV columns do not include commas inside fields.
        String[] parts = line.split(",");
        if (parts.length < 11) return; // invalid row

        // CSV Columns order:
        // 0: artist_name, 1: artist_id,
        // 2: labels_label_id, 3: labels_label_name,
        // 4: genres_genre, 5: release_title,
        // 6: release_master_id, 7: root_release_id,
        // 8: track_title, 9: track_duration, 10: styles_style

        // Replace empty values with defaults.
        String artistName       = parts[0].trim().isEmpty() ? "Unknown Artist" : parts[0].trim();
        String artistId         = parts[1].trim().isEmpty() ? "unknown_id" : parts[1].trim();
        String labelId          = parts[2].trim().isEmpty() ? "unknown_label" : parts[2].trim();
        String labelName        = parts[3].trim().isEmpty() ? "Unknown Label" : parts[3].trim();
        String genre            = parts[4].trim().isEmpty() ? "Unknown Genre" : parts[4].trim();
        String albumTitle       = parts[5].trim().isEmpty() ? "Untitled Album" : parts[5].trim();
        String releaseMasterId  = parts[6].trim().isEmpty() ? "0" : parts[6].trim();
        String rootReleaseId    = parts[7].trim().isEmpty() ? "unknown_album" : parts[7].trim();
        // Use rootReleaseId if releaseMasterId equals "0" or is empty.
        String albumId          = "0".equals(releaseMasterId) ? rootReleaseId : releaseMasterId;
        String trackTitle       = parts[8].trim().isEmpty() ? "Untitled Track" : parts[8].trim();
        String trackDurationStr = parts[9].trim().isEmpty() ? "0:00" : parts[9].trim();
        String style            = parts[10].trim().isEmpty() ? "Unknown Style" : parts[10].trim();

        // Get or create Artist.
        Artist artist = artistMap.get(artistId);
        if (artist == null) {
            artist = new Artist(artistId, artistName);
            artistMap.put(artistId, artist);
        }

        // Get or create Label.
        Label label = labelMap.get(labelId);
        if (label == null) {
            label = new Label(labelId, labelName);
            labelMap.put(labelId, label);
        }

        // Get or create Album.
        Album album = albumMap.get(albumId);
        if (album == null) {
            album = new Album(albumId, albumTitle, artist, label);
            album.getGenres().add(genre);
            album.getStyles().add(style);
            albumMap.put(albumId, album);
            albums.add(album);
        } else {
            album.getGenres().add(genre);
            album.getStyles().add(style);
        }

        // Create track.
        double duration = parseDuration(trackDurationStr);
        String trackId = albumId + "_" + album.getTracks().size();
        Track track = new Track(trackId, trackTitle, duration, artist, label);
        album.getTracks().add(track);
    }

    /**
     * Parses a duration string in the format "mm:ss" to minutes as a double.
     */
    private double parseDuration(String durationStr) {
        String[] timeParts = durationStr.split(":");
        if (timeParts.length != 2) return 0.0;
        try {
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);
            return minutes + seconds / 60.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
