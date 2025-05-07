package com.meloman.project.transaction_model;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

/**
 * Represents a music track, extending the MediaItem class.
 * Includes additional metadata such as duration.
 */
@Getter
@Setter
public class TrackT extends MediaItem implements Cloneable {

    /**
     * Duration of the track in minutes.
     */
    private double duration;

    /**
     * Constructs a new Track instance with the specified attributes.
     *
     * @param id       unique identifier of the track
     * @param title    title of the track
     * @param year     release year of the track
     * @param genres   set of genres associated with the track
     * @param styles   set of musical styles of the track
     * @param artistT   the artist associated with the track
     * @param labelT    the label that produced or published the track
     * @param duration the duration of the track in seconds
     */
    public TrackT(String id, String title, int year, Set<String> genres, Set<String> styles,
                  ArtistT artistT, LabelT labelT, double duration) {
        super(id, title, year, genres, styles, artistT, labelT);
        this.duration = duration;
    }

    public TrackT(String id, String trackTitle, double duration, ArtistT artistT, LabelT labelT) {
        super(id);
        this.title = trackTitle;
        this.duration = duration;
        this.artistT = artistT;
        this.labelT = labelT;
    }

    /**
     * Creates and returns a shallow clone of this Track.
     * Uses the cloning mechanism from the superclass.
     *
     * @return a clone of this Track
     */
    @Override
    public TrackT clone() {
        return (TrackT) super.clone();
    }

    /**
     * Displays basic information about the track to the console.
     * Includes the title and duration.
     */
    @Override
    public void displayInfo() {
        System.out.println("Track: " + getTitle() + " (" + duration + " seconds)");
    }

    /**
     * Returns a string representation of the Track object.
     *
     * @return a string containing the track ID, title, and duration
     */
    @Override
    public String toString() {
        return "Track [id=" + getId() + ", title=" + getTitle() + ", duration=" + duration + "]";
    }
}
