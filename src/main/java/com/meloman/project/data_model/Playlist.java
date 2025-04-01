package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a music playlist containing a set of tracks.
 * Stores metadata such as the number of albums, artists, and followers.
 */
@Getter
@Setter
public class Playlist {

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
        calculateNumAlbums();
        calculateNumArtists();
    }

    /**
     * Calculates the number of unique albums in the playlist based on track album IDs.
     */
    private void calculateNumAlbums() {
        Set<String> albumIds = new HashSet<>();
        for (Track track : tracks) {
            albumIds.add(track.getId()); // Assumes track ID represents album ID
        }
        this.numAlbums = albumIds.size();
    }

    /**
     * Calculates the number of unique artists in the playlist based on track artist names.
     */
    private void calculateNumArtists() {
        Set<String> artistNames = new HashSet<>();
        for (Track track : tracks) {
            if (track.getArtist() != null) {
                artistNames.add(track.getArtist().getName());
            }
        }
        this.numArtists = artistNames.size();
    }
}
