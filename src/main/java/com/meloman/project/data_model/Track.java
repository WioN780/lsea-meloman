package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class Track extends MediaItem implements Cloneable {
    private double duration;

    public Track(String id, String title, int year, Set<String> genres, Set<String> styles, Artist artist, Label label, double duration) {
        super(id, title, year, genres, styles, artist, label);
        this.duration = duration;
    }

    @Override
    public Track clone() {
        return (Track) super.clone();
    }

    @Override
    public void displayInfo() {
        System.out.println("Track: " + getTitle() + " (" + duration + " mins)");
    }

    @Override
    public String toString() {
        return "Track [id=" + getId() + ", title=" + getTitle() + ", duration=" + duration + "]";
    }
}
