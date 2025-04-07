package com.meloman.project.utils;


import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Artist;
import com.meloman.project.data_model.Label;
import com.meloman.project.data_model.Track;
import com.meloman.project.data_model.Playlist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class SpotifyPlaylistLoader {

    // Maps to avoid duplicate creations.
    private Map<String, Artist> artistMap = new HashMap<>();
    private Map<String, Album> albumMap = new HashMap<>();

    // Define static constants for default values
    private static final String UNKNOWN_ARTIST = "Unknown Artist";
    private static final String UNKNOWN_ID = "unknown_id";
    private static final String UNKNOWN_LABEL = "Unknown Label";
    private static final String UNKNOWN_LABEL_ID = "unknown_label";
    private static final String UNTITLED_ALBUM = "Untitled Album";
    private static final String UNKNOWN_ALBUM_ID = "unknown_album";
    private static final String UNTITLED_TRACK = "Untitled Track";
    private static final double DEFAULT_DURATION_MS = 0.0;

    /**
     * Loads playlists from the provided JSON file.
     * @param inputStream the input stream of the JSON file.
     * @return list of Playlist objects.
     * @throws IOException if an I/O error occurs.
     */
    public List<Playlist> loadPlaylists(InputStream inputStream) throws IOException {
        List<Playlist> playlists = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonString = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        reader.close();

        // Parse the JSON string
        JSONObject root = new JSONObject(jsonString.toString());
        JSONObject info = root.optJSONObject("info");
        JSONArray playlistsArray = root.optJSONArray("playlists");

        if (playlistsArray != null) {
            int totalPlaylists = playlistsArray.length();
            System.out.println("Total playlists found: " + totalPlaylists);

            for (int i = 0; i < totalPlaylists; i++) {
                JSONObject playlistJson = playlistsArray.getJSONObject(i);
                Playlist playlist = processPlaylist(playlistJson);
                if (playlist != null) {
                    playlists.add(playlist);
                }

                if ((i + 1) % 100 == 0) {
                    System.out.println("Processed " + (i + 1) + " playlists");
                }
            }
        }

        System.out.println("Total playlists loaded: " + playlists.size());
        return playlists;
    }

    /**
     * Process a single playlist JSON object
     * @param playlistJson JSON object representing a playlist
     * @return a Playlist object
     */
    private Playlist processPlaylist(JSONObject playlistJson) {
        // Extract playlist info
        String name = playlistJson.optString("name", "Unnamed Playlist");
        boolean collaborative = playlistJson.optBoolean("collaborative", false);
        String pid = playlistJson.optString("pid", UNKNOWN_ID);
        long modifiedAt = playlistJson.optLong("modified_at", 0);
        int numFollowers = playlistJson.optInt("num_followers", 0);

        // Process tracks
        JSONArray tracksArray = playlistJson.optJSONArray("tracks");
        Set<Track> tracks = new HashSet<>();

        if (tracksArray != null) {
            for (int i = 0; i < tracksArray.length(); i++) {
                JSONObject trackJson = tracksArray.getJSONObject(i);
                Track track = processTrack(trackJson);
                if (track != null) {
                    tracks.add(track);
                }
            }
        }

        // Create and return the playlist
        return new Playlist(tracks, numFollowers);
    }

    /**
     * Process a single track JSON object
     * @param trackJson JSON object representing a track
     * @return a Track object
     */
    private Track processTrack(JSONObject trackJson) {
        int position = trackJson.optInt("pos", 0);
        String artistName = trackJson.optString("artist_name", UNKNOWN_ARTIST);
        String trackName = trackJson.optString("track_name", UNTITLED_TRACK);
        double durationMs = trackJson.optDouble("duration_ms", DEFAULT_DURATION_MS);
        String albumName = trackJson.optString("album_name", UNTITLED_ALBUM);

        // Convert duration from ms to seconds
        double durationSecs = durationMs / 1000.0;

        // Generate IDs for objects that don't have them in the JSON
        String artistId = generateArtistId(artistName);
        String albumId = generateAlbumId(albumName, artistName);
        String trackId = generateTrackId(trackName, albumId);

        // Get or create Artist
        Artist artist = artistMap.get(artistId);
        if (artist == null) {
            artist = new Artist(artistId, artistName);
            artistMap.put(artistId, artist);
        }

        // Create a default label
        Label label = new Label(UNKNOWN_LABEL_ID, UNKNOWN_LABEL);

        // Get or create Album
        Album album = albumMap.get(albumId);
        if (album == null) {
            album = new Album(albumId, albumName, artist, label);
            albumMap.put(albumId, album);
        }

        // Create and return the track
        Track track = new Track(trackId, trackName, durationSecs, artist, label);
        return track;
    }

    /**
     * Generate a consistent ID for an artist based on name
     */
    private String generateArtistId(String artistName) {
        return "artist_" + artistName.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }

    /**
     * Generate a consistent ID for an album based on name and artist
     */
    private String generateAlbumId(String albumName, String artistName) {
        return "album_" + artistName.toLowerCase().replaceAll("[^a-z0-9]", "_") +
                "_" + albumName.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }

    /**
     * Generate a consistent ID for a track based on name and album ID
     */
    private String generateTrackId(String trackName, String albumId) {
        return albumId + "_track_" + trackName.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }
}