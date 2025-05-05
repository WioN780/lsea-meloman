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
@Table(name = "ALBUM")
public class Album implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "`year`")
    private int year;

    @Column(name = "contact_info", length = 500)
    private String contactInfo;

    @Column(name = "url", length = 500)
    private String url;

    @ManyToMany
    @JoinTable(
            name = "album_genre",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "album_style",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<Style> styles = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;

    @ManyToOne(optional = false)
    @JoinColumn(name = "label_id", referencedColumnName = "id")
    private Label label;

    public Album() {}

    public Album(String id, String title, int year, String contactInfo, String url, Set<Genre> genres, Set<Style> styles,  Artist artist, Label label) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.contactInfo = contactInfo;
        this.url = url;
        this.genres = genres;
        this.styles = styles;
        this.artist = artist;
        this.label = label;
    }
}