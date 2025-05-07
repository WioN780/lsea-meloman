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
public class AlbumT extends MediaItem {

    private List<TrackT> trackTS;

    /**
     * Constructs an Album object with the specified details.
     *
     * @param id The unique identifier of the album.
     * @param title The title of the album.
     * @param year The year the album was released.
     * @param genres A set of genres associated with the album.
     * @param styles A set of styles associated with the album.
     * @param artistT The artist who created the album.
     * @param labelT The label associated with the album.
     * @param trackTS A list of tracks in the album.
     */

    public AlbumT(String id, String title, int year, Set<String> genres, Set<String> styles, ArtistT artistT, LabelT labelT, List<TrackT> trackTS) {
        super(id, title, year, genres, styles, artistT, labelT);
        this.trackTS = trackTS;
    }

    public AlbumT(String id, String title, ArtistT artistT, LabelT labelT) {
        super(id, title, artistT, labelT);
        this.trackTS = new ArrayList<TrackT>();
    }

    /**
     * Displays information about the album, including the title, artist, year, genres,
     * and the list of tracks in the album.
     */

    @Override
    public void displayInfo() {
        System.out.println("Album: " + getTitle() + " by " + artistT.getName());
        System.out.println("Year: " + getYear());
        System.out.println("Genres: " + getGenres());
        System.out.println("Tracks:");
        int i = 1;
        for (TrackT trackT : trackTS) {
            System.out.println(" - " + i + ": " + trackT.getTitle());
            i++;
        }
    }

    /**
     * Creates a deep clone of the album, including cloning the list of tracks.
     *
     * @return A new cloned Album object.
     */

    @Override
    public AlbumT clone() {
        AlbumT cloned = (AlbumT) super.clone();
        List<TrackT> clonedTrackTS = new ArrayList<>();
        for (TrackT trackT : this.trackTS) {
            clonedTrackTS.add(trackT.clone());
        }
        cloned.setTrackTS(clonedTrackTS);
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
        String trackList = IntStream.range(0, trackTS.size())
                .mapToObj(i -> (i + 1) + ". " + trackTS.get(i).toString())
                .collect(Collectors.joining(";\n"));

        return "Album [id=" + getId() + ", title=" + getTitle() + ", artist=" + artistT.getName() + ", label=" + labelT.getName() +
                ", year=" + getYear() + ", genres=" + getGenres() + ", styles=" + getStyles() +
                ", tracks:\n" + trackList + "]";
    }
    
}