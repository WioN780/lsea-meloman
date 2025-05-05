package com.meloman.project.utils_database;

import com.meloman.project.database_model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.*;
import java.util.*;

public class DiscoGSLoader_database {

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
    private static final int MAX_ID_LENGTH = 50; // Maximum length for ID fields

    private EntityManagerFactory emf;
    private EntityManager em;

    // Cache to avoid duplicates, maybe later we dont need them with good IDs
    private final Map<String, Artist> artistMap = new HashMap<>();
    private final Map<String, Label> labelMap = new HashMap<>();
    private final Map<String, Album> albumMap = new HashMap<>();
    private final Map<String, Genre> genreMap = new HashMap<>();
    private final Map<String, Style> styleMap = new HashMap<>();

    public DiscoGSLoader_database(String persistenceUnitName) {
        this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.em = emf.createEntityManager();
    }

    /**
     * Imports data from a CSV file into the database using JPA.
     * @param csvFilePath path to the CSV file
     * @throws IOException if an I/O error occurs
     */
    public void importFromCSV(String csvFilePath) throws IOException {
        importFromCSV(new FileInputStream(csvFilePath));
    }

    /**
     * Imports data from a CSV input stream into the database using JPA.
     * @param inputStream the input stream of the CSV file
     * @throws IOException if an I/O error occurs
     */
    public void importFromCSV(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // Skip the header row
        reader.readLine();

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            int processedLines = 0;

            while ((line = reader.readLine()) != null) {
                processLine(line);
                processedLines++;

                if (processedLines % 100 == 0) {
                    System.out.println("Processed " + processedLines + " lines");
                    System.out.println("Albums: " + albumMap.size());
                }
            }

            transaction.commit();
            System.out.println("CSV import completed successfully");
            System.out.println("Total processed lines: " + processedLines);
            System.out.println("Total albums: " + albumMap.size());
            System.out.println("Total artists: " + artistMap.size());
            System.out.println("Total labels: " + labelMap.size());
            System.out.println("Total genres: " + genreMap.size());
            System.out.println("Total styles: " + styleMap.size());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Error during CSV import: " + e.getMessage());
        } finally {
            reader.close();
        }
    }

    private void processLine(String line) {
        if (line == null || line.trim().isEmpty()) return;
        String[] parts = line.split(",");
        if (parts.length < 11) return; // invalid row

        // CSV Columns order:
        // 0: artist_name, 1: artist_id,
        // 2: labels_label_id, 3: labels_label_name,
        // 4: genres_genre, 5: release_title,
        // 6: release_master_id, 7: root_release_id,
        // 8: track_title, 9: track_duration, 10: styles_style

        String artistName = parts[0].trim().isEmpty() ? UNKNOWN_ARTIST : parts[0].trim();
        String artistId = parts[1].trim().isEmpty() ? UNKNOWN_ID : parts[1].trim();
        String labelId = parts[2].trim().isEmpty() ? UNKNOWN_LABEL_ID : parts[2].trim();
        String labelName = parts[3].trim().isEmpty() ? UNKNOWN_LABEL : parts[3].trim();
        String genreName = parts[4].trim().isEmpty() ? UNKNOWN_GENRE : parts[4].trim();
        String albumTitle = parts[5].trim().isEmpty() ? UNTITLED_ALBUM : parts[5].trim();
        String releaseMasterId = parts[6].trim().isEmpty() ? "0" : parts[6].trim();
        String rootReleaseId = parts[7].trim().isEmpty() ? UNKNOWN_ALBUM_ID : parts[7].trim();
        String albumId = "0".equals(releaseMasterId) ? rootReleaseId : releaseMasterId;
        String trackTitle = parts[8].trim().isEmpty() ? UNTITLED_TRACK : parts[8].trim();
        String trackDurationStr = parts[9].trim().isEmpty() ? DEFAULT_TRACK_DURATION : parts[9].trim();
        String styleName = parts[10].trim().isEmpty() ? UNKNOWN_STYLE : parts[10].trim();

        // Get or create Artist
        Artist artist = getOrCreateArtist(artistId, artistName);

        // Get or create Label
        Label label = getOrCreateLabel(labelId, labelName);

        // Get or create Genre
        Genre genre = getOrCreateGenre(genreName);

        // Get or create Style
        Style style = getOrCreateStyle(styleName);

        // Create sets for relationships
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        Set<Style> styles = new HashSet<>();
        styles.add(style);

        // Get or create Album
        Album album = getOrCreateAlbum(albumId, albumTitle, artist, label);

        // Add genre and style to album if not already present
        if (!album.getGenres().contains(genre)) {
            album.getGenres().add(genre);
        }
        if (!album.getStyles().contains(style)) {
            album.getStyles().add(style);
        }

        // Get current year as a fallback
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Create a unique track ID using a combination of album, artist, title and duration
        // This ensures uniqueness even if the same track appears in multiple places
        double duration = parseDuration(trackDurationStr);
        String trackId = albumId + "_" + artist.getId() + "_" +
                trackTitle.replace(" ", "_").toLowerCase();

        // Truncate track ID to maximum length if needed
        if (trackId.length() > MAX_ID_LENGTH) {
            trackId = trackId.substring(0, MAX_ID_LENGTH);
        }

        // Create a set of artists for the track
        Set<Artist> trackArtists = new HashSet<>();
        trackArtists.add(artist);

        // Check if track already exists in database
        Track existingTrack = em.find(Track.class, trackId);
        if (existingTrack == null) {
            Track track = new Track(trackId, trackTitle, currentYear, genres, styles, trackArtists, label);
            track.setDuration(duration);
            em.persist(track);
        }
    }

    private Artist getOrCreateArtist(String id, String name) {
        if (artistMap.containsKey(id)) {
            return artistMap.get(id);
        }

        Artist artist = em.find(Artist.class, id);
        if (artist == null) {
            artist = new Artist(id, name, "", "", "");
            em.persist(artist);
        }

        artistMap.put(id, artist);
        return artist;
    }

    private Label getOrCreateLabel(String id, String name) {
        if (labelMap.containsKey(id)) {
            return labelMap.get(id);
        }

        Label label = em.find(Label.class, id);
        if (label == null) {
            label = new Label(id, name, "", "");
            em.persist(label);
        }

        labelMap.put(id, label);
        return label;
    }

    private Genre getOrCreateGenre(String name) {
        // Using name as ID for simplicity
        String id = name.toLowerCase().replace(" ", "_");

        if (genreMap.containsKey(id)) {
            return genreMap.get(id);
        }

        Genre genre = em.find(Genre.class, id);
        if (genre == null) {
            genre = new Genre(id, name);
            em.persist(genre);
        }

        genreMap.put(id, genre);
        return genre;
    }

    private Style getOrCreateStyle(String name) {
        // Using name as ID for simplicity
        String id = name.toLowerCase().replace(" ", "_");

        if (styleMap.containsKey(id)) {
            return styleMap.get(id);
        }

        Style style = em.find(Style.class, id);
        if (style == null) {
            style = new Style(id, name);
            em.persist(style);
        }

        styleMap.put(id, style);
        return style;
    }

    private Album getOrCreateAlbum(String id, String title, Artist artist, Label label) {
        if (albumMap.containsKey(id)) {
            return albumMap.get(id);
        }

        Album album = em.find(Album.class, id);
        if (album == null) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            album = new Album(id, title, year, "", "", new HashSet<>(), new HashSet<>(), artist, label);
            em.persist(album);
        }

        albumMap.put(id, album);
        return album;
    }

    private double parseDuration(String durationStr) {
        try {
            String[] parts = durationStr.split(":");
            if (parts.length != 2) return 0.0;
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60.0 + seconds;
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Prints sample data from the database for verification
     */
    public void printSampleData() {
        System.out.println("Sample Data from Database");

        // Print artists
        System.out.println("Artists");
        List<Artist> artists = em.createQuery("SELECT a FROM Artist a", Artist.class)
                .setMaxResults(5)
                .getResultList();
        for (Artist artist : artists) {
            System.out.println("ID: " + artist.getId() + ", Name: " + artist.getName());
        }

        // Print albums
        System.out.println("Albums");
        List<Album> albums = em.createQuery("SELECT a FROM Album a", Album.class)
                .setMaxResults(5)
                .getResultList();
        for (Album album : albums) {
            System.out.println("ID: " + album.getId() + ", Title: " + album.getTitle() +
                    ", Artist: " + album.getArtist().getName());
        }

        // Print tracks
        System.out.println("Tracks");
        List<Track> tracks = em.createQuery("SELECT t FROM Track t", Track.class)
                .setMaxResults(5)
                .getResultList();
        for (Track track : tracks) {
            System.out.println("ID: " + track.getId() + ", Title: " + track.getTitle() +
                    ", Duration: " + track.getDuration() + "s");
            System.out.println("  Genres: " + getNamesList(track.getGenres()));
            System.out.println("  Styles: " + getNamesList(track.getStyles()));
        }

        // Print genres
        System.out.println("Genres");
        List<Genre> genres = em.createQuery("SELECT g FROM Genre g", Genre.class)
                .setMaxResults(5)
                .getResultList();
        for (Genre genre : genres) {
            System.out.println("ID: " + genre.getId() + ", Name: " + genre.getName());
        }

        // Print styles
        System.out.println("Styles");
        List<Style> styles = em.createQuery("SELECT s FROM Style s", Style.class)
                .setMaxResults(5)
                .getResultList();
        for (Style style : styles) {
            System.out.println("ID: " + style.getId() + ", Name: " + style.getName());
        }

        System.out.println("End of Sample Data");
    }

    // Helper method to get a comma-separated list of names from a collection
    private <T> String getNamesList(Set<T> items) {
        if (items == null || items.isEmpty()) return "None";

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (T item : items) {
            if (!first) {
                sb.append(", ");
            }
            if (item instanceof Genre) {
                sb.append(((Genre) item).getName());
            } else if (item instanceof Style) {
                sb.append(((Style) item).getName());
            }
            first = false;
        }

        return sb.toString();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java DiscoGSLoader_database <persistence-unit-name> <csv-file-path>");
            return;
        }

        String persistenceUnitName = args[0];
        String csvFilePath = args[1];

        DiscoGSLoader_database importer = new DiscoGSLoader_database(persistenceUnitName);
        try {
            importer.importFromCSV(csvFilePath);
            // Print sample data after successful import
            importer.printSampleData();
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } finally {
            importer.close();
        }
    }
}