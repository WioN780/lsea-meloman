package com.meloman.project.transaction_model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a music playlist containing a set of tracks.
 * Stores metadata such as name, collaborative status, ID, modification date,
 * and the number of albums, artists, and followers.
 */
@Getter
@Setter
public class Playlist {

    /**
     * Unique identifier for the playlist.
     */
    @Setter
    private String pid;

    /**
     * Name of the playlist.
     */
    @Setter
    private String name;

    /**
     * Whether the playlist is collaborative.
     */
    @Setter
    private boolean collaborative;

    /**
     * Timestamp when the playlist was last modified.
     */
    @Setter
    private long modifiedAt;

    /**
     * Set of tracks included in the playlist.
     */
    private Set<Track> tracks;

    /**
     * Total number of unique albums in the playlist.
     */
    private int numAlbums;

    /**
     * Total number of unique artists in the playlist.
     */
    private int numArtists;

    /**
     * Number of users following the playlist.
     */
    private int numFollowers;

    /**
     * Constructs a Playlist with the given set of tracks and number of followers.
     * Automatically calculates the number of unique albums and artists.
     *
     * @param tracks        the set of tracks in the playlist
     * @param numFollowers  the number of followers for the playlist
     */
    public Playlist(Set<Track> tracks, int numFollowers) {
        this.tracks = tracks;
        this.numFollowers = numFollowers;
        this.name = "Unnamed Playlist";
        this.pid = "unknown_pid";
        this.collaborative = false;
        this.modifiedAt = 0;
        calculateNumAlbums();
        calculateNumArtists();
    }

    /**
     * Constructs a Playlist with all metadata and tracks.
     *
     * @param pid            the playlist ID
     * @param name           the playlist name
     * @param collaborative  whether the playlist is collaborative
     * @param modifiedAt     timestamp when the playlist was last modified
     * @param tracks         the set of tracks in the playlist
     * @param numFollowers   the number of followers for the playlist
     */
    public Playlist(String pid, String name, boolean collaborative, long modifiedAt,
                    Set<Track> tracks, int numFollowers) {
        this.pid = pid;
        this.name = name;
        this.collaborative = collaborative;
        this.modifiedAt = modifiedAt;
        this.tracks = tracks;
        this.numFollowers = numFollowers;
        calculateNumAlbums();
        calculateNumArtists();
    }

    /**
     * Calculates the number of unique albums in the playlist based on track album IDs.
     */
    private void calculateNumAlbums() {
        Set<String> albumIds = new HashSet<>();
        for (Track track : tracks) {
            // Extract album ID from track ID (assuming track ID format includes album ID)
            String albumId = extractAlbumId(track.getId());
            albumIds.add(albumId);
        }
        this.numAlbums = albumIds.size();
    }

    /**
     * Extracts album ID from a track ID.
     * Assumes track ID format is "album_[albumId]_track_[trackName]"
     */
    private String extractAlbumId(String trackId) {
        // If track ID contains "_track_", extract the album part
        if (trackId.contains("_track_")) {
            return trackId.substring(0, trackId.indexOf("_track_"));
        }
        return trackId; // Fallback
    }

    /**
     * Calculates the number of unique artists in the playlist based on track artist IDs.
     */
    private void calculateNumArtists() {
        Set<String> artistIds = new HashSet<>();
        for (Track track : tracks) {
            artistIds.add(track.getArtist().getId());
        }
        this.numArtists = artistIds.size();
    }


    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public long getModifiedAt() {
        return modifiedAt;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
        calculateNumAlbums();
        calculateNumArtists();
    }

    public int getNumAlbums() {
        return numAlbums;
    }

    public int getNumArtists() {
        return numArtists;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumTracks() {
        return tracks.size();
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", collaborative=" + collaborative +
                ", modifiedAt=" + modifiedAt +
                ", numTracks=" + getNumTracks() +
                ", numAlbums=" + numAlbums +
                ", numArtists=" + numArtists +
                ", numFollowers=" + numFollowers +
                '}';
    }
}
