package com.meloman.project.utils;

import com.meloman.project.transaction_model.AlbumT;
import com.meloman.project.transaction_model.ArtistT;
import com.meloman.project.transaction_model.LabelT;
import com.meloman.project.transaction_model.TrackT;

import java.io.*;
import java.util.*;

public class DiscoGSLoader {

    // Maps to avoid duplicate creations.
    private Map<String, ArtistT> artistMap = new HashMap<>();
    private Map<String, LabelT> labelMap = new HashMap<>();
    private Map<String, AlbumT> albumMap = new HashMap<>();

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
    public List<AlbumT> loadAlbums(InputStream inputStream, int maxAlbums) throws IOException {
        List<AlbumT> albumTS = new ArrayList<>();
        int processedLines = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // Skip the header row.
        reader.readLine();

        while ((line = reader.readLine()) != null && albumTS.size() < maxAlbums) {
            processLineDiscoGS(line, albumTS);
            processedLines++;

            if (processedLines % 10000 == 0) { //Limit for now until further optimization.
                //System.out.println(processedLines + " linea prozetatutak.");
                //System.out.println(albums.get(albums.size() - 1).toString());
                System.out.println("Album guztiak: " + albumTS.size());
            }
        }
        reader.close();
        System.out.println("Albumes guztiak: " + albumTS.size());
        System.out.println("Linea guztiak: " + processedLines);
        return albumTS;
    }

    /**
     * Loads albums from the provided CSV InputStream.
     * @param inputStream the input stream of the CSV file.
     * @return list of Album objects.
     * @throws IOException if an I/O error occurs.
     */
    public List<AlbumT> loadAlbums(InputStream inputStream) throws IOException {
        List<AlbumT> albumTS = new ArrayList<>();
        int processedLines = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // Skip the header row.
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            processLineDiscoGS(line, albumTS);
            processedLines++;

            if (processedLines % 100 == 0) { //Limit for now until further optimization.
                //System.out.println(processedLines + " linea prozetatutak.");
                //System.out.println(albums.get(albums.size() - 1).toString());
                System.out.println("Album guztiak: " + albumTS.size());
            }
        }
        reader.close();
        System.out.println("Albumes guztiak: " + albumTS.size());
        System.out.println("Linea guztiak: " + processedLines);
        return albumTS;
    }

    private void processLineDiscoGS(String line, List<AlbumT> albumTS) {
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
        ArtistT artistT = artistMap.get(artistId);
        if (artistT == null) {
            artistT = new ArtistT(artistId, artistName);
            artistMap.put(artistId, artistT);
        }

        // Get or create Label.
        LabelT labelT = labelMap.get(labelId);
        if (labelT == null) {
            labelT = new LabelT(labelId, labelName);
            labelMap.put(labelId, labelT);
        }

        // Get or create Album.
        AlbumT albumT = albumMap.get(albumId);
        if (albumT == null) {
            albumT = new AlbumT(albumId, albumTitle, artistT, labelT);
            albumT.getGenres().add(genre);
            albumT.getStyles().add(style);
            albumMap.put(albumId, albumT);
            albumTS.add(albumT);
        } else {
            albumT.getGenres().add(genre);
            albumT.getStyles().add(style);
        }

        // Create track.
        double duration = parseDuration(trackDurationStr);
        String trackId = albumId + "_" + albumT.getTrackTS().size();
        TrackT trackT = new TrackT(trackId, trackTitle, duration, artistT, labelT);
        albumT.getTrackTS().add(trackT);
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
