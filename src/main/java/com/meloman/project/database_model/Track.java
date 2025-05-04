package com.meloman.project.database_model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TRACK")
public class Track implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "duration")
    private double duration;

    @Column(name = "year")
    private int year;

    @ManyToMany
    @JoinTable(
            name = "track_genre",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "track_style",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<Style> styles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "artist_track",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private Set<Track> artists = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "label_id", referencedColumnName = "id")
    private Label label;

    public Track() {}

    public Track(String id, String title, int year, Set<Genre> genres, Set<Style> styles, Set<Track> artists, Label label) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.styles = styles;
        this.artists = artists;
        this.label = label;
    }
}