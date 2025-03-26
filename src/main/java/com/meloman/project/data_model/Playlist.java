package com.meloman.project.data_model;

import java.util.HashSet;
import java.util.Set;

public class Playlist {

    private Set<Track> tracks;
    private int numAlbums;
    private int numArtists;
    private int numFollowers;

    public Playlist(Set<Track> tracks, int numFollowers) {
        this.tracks = tracks;
        this.numFollowers = numFollowers;
        calculateNumAlbums();
        calculateNumArtists();
    }

    private void calculateNumAlbums() {
        Set<String> albumIds = new HashSet<>();
        for (Track track : tracks) {
            albumIds.add(track.getId());
        }
        this.numAlbums = albumIds.size();
    }

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