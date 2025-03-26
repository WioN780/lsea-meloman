package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Abstract class representing a general media item such as an album, single, or compilation.
 * <p>
 * This class serves as a base for different types of media and includes common metadata
 * such as title, release year, genres, styles, artist, and label.
 * </p>
 */
@Getter
@Setter
public abstract class MediaItem implements Cloneable, Comparable<MediaItem> {

    /** Unique identifier for the media item. */
    protected String id;

    /** Title of the media item. */
    protected String title;

    /** Release year of the media item. */
    protected int year;

    /** Set of genres associated with the media item (e.g., Rock, Jazz). */
    protected Set<String> genres;

    /** Set of styles associated with the media item (e.g., Psychedelic Rock, Cool Jazz). */
    protected Set<String> styles;

    /** Artist associated with the media item. */
    protected Artist artist;

    /** Label that published the media item. */
    protected Label label;

    /**
     * Constructs a MediaItem with the provided metadata.
     *
     * @param id     the unique identifier of the media item
     * @param title  the title of the media item
     * @param year   the year the media item was released
     * @param genres the set of genres associated with the item
     * @param styles the set of styles associated with the item
     * @param artist the artist who performed or created the media
     * @param label  the label that published the media
     */
    public MediaItem(String id, String title, int year, Set<String> genres, Set<String> styles,
                     Artist artist, Label label) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.styles = styles;
        this.artist = artist;
        this.label = label;
    }

    /**
     * Abstract method for displaying media item information.
     * Must be implemented by subclasses to define how the item is presented.
     */
    public abstract void displayInfo();

    /**
     * Compares this MediaItem to another based on their release year.
     *
     * @param other the other MediaItem to compare with
     * @return a negative integer, zero, or a positive integer if this item's year
     *         is less than, equal to, or greater than the specified item's year
     */
    @Override
    public int compareTo(MediaItem other) {
        return Integer.compare(this.year, other.year);
    }

    /**
     * Creates and returns a shallow copy of this MediaItem.
     *
     * @return a clone of this instance
     * @throws AssertionError if the cloning is not supported
     */
    @Override
    public MediaItem clone() {
        try {
            return (MediaItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Returns a string representation of the MediaItem.
     *
     * @return a string containing media item details
     */
    @Override
    public String toString() {
        return "MediaItem [id=" + id + ", title=" + title + ", year=" + year +
                ", genres=" + genres + ", styles=" + styles +
                ", artist=" + artist.getName() + ", label=" + label.getName() + "]";
    }
}
