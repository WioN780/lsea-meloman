package com.meloman.project.transaction_model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

/**
 * Represents a music album.
 * The Album class contains information about the album's title, year, genres,
 * styles, artist, label, and tracks. It also provides functionality to display
 * information about the album and clone it.
 */

@Getter
@Setter
public class Album extends MediaItem {

    private List<Track> tracks;

    /**
     * Constructs an Album object with the specified details.
     *
     * @param id The unique identifier of the album.
     * @param title The title of the album.
     * @param year The year the album was released.
     * @param genres A set of genres associated with the album.
     * @param styles A set of styles associated with the album.
     * @param artist The artist who created the album.
     * @param label The label associated with the album.
     * @param tracks A list of tracks in the album.
     */

    public Album(String id, String title, int year, Set<String> genres, Set<String> styles, Artist artist, Label label, List<Track> tracks) {
        super(id, title, year, genres, styles, artist, label);
        this.tracks = tracks;
    }

    public Album(String id, String title, Artist artist, Label label) {
        super(id, title, artist, label);
        this.tracks = new ArrayList<Track>();
    }

    /**
     * Displays information about the album, including the title, artist, year, genres,
     * and the list of tracks in the album.
     */

    @Override
    public void displayInfo() {
        System.out.println("Album: " + getTitle() + " by " + artist.getName());
        System.out.println("Year: " + getYear());
        System.out.println("Genres: " + getGenres());
        System.out.println("Tracks:");
        int i = 1;
        for (Track track : tracks) {
            System.out.println(" - " + i + ": " + track.getTitle());
            i++;
        }
    }

    /**
     * Creates a deep clone of the album, including cloning the list of tracks.
     *
     * @return A new cloned Album object.
     */

    @Override
    public Album clone() {
        Album cloned = (Album) super.clone();
        List<Track> clonedTracks = new ArrayList<>();
        for (Track track : this.tracks) {
            clonedTracks.add(track.clone());
        }
        cloned.setTracks(clonedTracks);
        return cloned;
    }

    /**
     * Returns a string representation of the album, including the album's ID, title,
     * artist name, label name, year, genres, styles, and track list.
     *
     * @return A string describing the album.
     */

    @Override
    public String toString() {
        String trackList = IntStream.range(0, tracks.size())
                .mapToObj(i -> (i + 1) + ". " + tracks.get(i).toString())
                .collect(Collectors.joining(";\n"));

        return "Album [id=" + getId() + ", title=" + getTitle() + ", artist=" + artist.getName() + ", label=" + label.getName() +
                ", year=" + getYear() + ", genres=" + getGenres() + ", styles=" + getStyles() +
                ", tracks:\n" + trackList + "]";
    }
    
}