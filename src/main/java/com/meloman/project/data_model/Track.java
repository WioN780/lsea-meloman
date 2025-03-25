package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track implements Cloneable {

    private int trackNumber;
    private String title;
    private double duration;

    public Track(int trackNumber, String title, double duration) {
        this.trackNumber = trackNumber;
        this.title = title;
        this.duration = duration;
    }

    @Override
    public Track clone() {
        try {
            return (Track) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Track [trackNumber=" + trackNumber + ", title=" + title + ", duration=" + duration + "]";
    }
}