package com.meloman.project.utils;

import com.meloman.project.transaction_model.Album;
import com.meloman.project.transaction_model.Artist;
import com.meloman.project.transaction_model.Label;
import com.meloman.project.transaction_model.Track;

import java.io.*;
import java.util.*;

public class DiscoGSLoader {

    // Maps to avoid duplicate creations.
    private Map<String, Artist> artistMap = new HashMap<>();
    private Map<String, Label> labelMap = new HashMap<>();
    private Map<String, Album> albumMap = new HashMap<>();

    // Define static constants for default values
    private static final String UNKNOWN_ARTIST = "Unknown Artist";
    private static final String UNKNOWN_ID = "unknown_id";
    private static final String UNKNOWN_LABEL = "Unknown Label";
    private static final String UNKNOWN_LABEL_ID = "unknown_label";
    private static final String UNKNOWN_GENRE = "Unknown Genre";
    private static final String UNTITLED_ALBUM = "Untitled Album";
    private static final String UNKNOWN_ALBUM_ID = "unknown_album";
    private static final String UNTITLED_TRACK = "Untitled Track";
    private static final String DEFAULT_TRACK_DURATION = "0:00";
    private static final String UNKNOWN_STYLE = "Unknown Style";

    /**
     * Loads albums from the provided CSV InputStream.
     * @param inputStream the input stream of the CSV file.
     * @param maxAlbums Maximum number of albums to be loaded.
     * @return list of Album objects.
     * @throws IOException if an I/O error occurs.
     */
    public List<Album> loadAlbums(InputStream inputStream, int maxAlbums) throws IOException {
        List<Album> albums = new ArrayList<>();
        int processedLines = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // Skip the header row.
        reader.readLine();

        while ((line = reader.readLine()) != null && albums.size() < maxAlbums) {
            processLineDiscoGS(line, albums);
            processedLines++;

            if (processedLines % 10000 == 0) { //Limit for now until further optimization.
                //System.out.println(processedLines + " linea prozetatutak.");
                //System.out.println(albums.get(albums.size() - 1).toString());
                System.out.println("Album guztiak: " + albums.size());
            }
        }
        reader.close();
        System.out.println("Albumes guztiak: " + albums.size());
        System.out.println("Linea guztiak: " + processedLines);
        return albums;
    }

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
            processLineDiscoGS(line, albums);
            processedLines++;

            if (processedLines % 100 == 0) { //Limit for now until further optimization.
                //System.out.println(processedLines + " linea prozetatutak.");
                //System.out.println(albums.get(albums.size() - 1).toString());
                System.out.println("Album guztiak: " + albums.size());
            }
        }
        reader.close();
        System.out.println("Albumes guztiak: " + albums.size());
        System.out.println("Linea guztiak: " + processedLines);
        return albums;
    }

    private void processLineDiscoGS(String line, List<Album> albums) {
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


        String artistName       = parts[0].trim().isEmpty() ? UNKNOWN_ARTIST : parts[0].trim();
        String artistId         = parts[1].trim().isEmpty() ? UNKNOWN_ID : parts[1].trim();
        String labelId          = parts[2].trim().isEmpty() ? UNKNOWN_LABEL_ID : parts[2].trim();
        String labelName        = parts[3].trim().isEmpty() ? UNKNOWN_LABEL : parts[3].trim();
        String genre            = parts[4].trim().isEmpty() ? UNKNOWN_GENRE : parts[4].trim();
        String albumTitle       = parts[5].trim().isEmpty() ? UNTITLED_ALBUM : parts[5].trim();
        String releaseMasterId  = parts[6].trim().isEmpty() ? "0" : parts[6].trim();
        String rootReleaseId    = parts[7].trim().isEmpty() ? UNKNOWN_ALBUM_ID : parts[7].trim();
        // Use rootReleaseId if releaseMasterId equals "0" or is empty.
        String albumId          = "0".equals(releaseMasterId) ? rootReleaseId : releaseMasterId;
        String trackTitle       = parts[8].trim().isEmpty() ? UNTITLED_TRACK : parts[8].trim();
        String trackDurationStr = parts[9].trim().isEmpty() ? DEFAULT_TRACK_DURATION : parts[9].trim();
        String style            = parts[10].trim().isEmpty() ? UNKNOWN_STYLE : parts[10].trim();

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
            return minutes * 60.0 + seconds;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
