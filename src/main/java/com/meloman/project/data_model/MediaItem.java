package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Abstract class representing a general media item.
 */
@Getter
@Setter
public abstract class MediaItem implements Cloneable {

    protected String id;
    protected String title;
    protected int year;
    protected List<String> genres;
    protected List<String> styles;
    protected Artist artist;
    protected Label label;

    public MediaItem(String id, String title, int year, List<String> genres, List<String> styles,
                     Artist artist, Label label) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.styles = styles;
        this.artist = artist;
        this.label = label;
    }

    public abstract void displayInfo();

    @Override
    public MediaItem clone() {
        try {
            return (MediaItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "MediaItem [id=" + id + ", title=" + title + ", year=" + year +
                ", genres=" + genres + ", styles=" + styles +
                ", artist=" + artist.getName() + ", label=" + label.getName() + "]";
    }
}
