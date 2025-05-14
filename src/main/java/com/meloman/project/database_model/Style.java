package com.meloman.project.database_model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "STYLE")
public class Style implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "name", length = 255)
    private String name;

    @ManyToMany(mappedBy = "styles")
    private Set<Track> tracks = new HashSet<>();

    @ManyToMany(mappedBy = "styles")
    private Set<Album> albums = new HashSet<>();

    public Style(){ }

    public Style(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
