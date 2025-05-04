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
@Table(name = "PLAYLIST")
public class Playlist implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "collaborative")
    private boolean collaborative;

    @Column(name = "modified_at")
    private long modifiedAt;

    @Column(name = "num_followers")
    private int numFollowers;

    @ManyToMany
    @JoinTable(
            name = "playlist_track",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private Set<Track> tracks = new HashSet<>();

    public Playlist() {}

    public Playlist(String id, String name, boolean collaborative, long modifiedAt, int numFollowers) {
        this.id = id;
        this.name = name;
        this.collaborative = collaborative;
        this.modifiedAt = modifiedAt;
        this.numFollowers = numFollowers;
        this.tracks = new HashSet<>();
    }

    public Playlist(String id, String name, boolean collaborative, long modifiedAt, int numFollowers, Set<Track> tracks) {
        this.id = id;
        this.name = name;
        this.collaborative = collaborative;
        this.modifiedAt = modifiedAt;
        this.numFollowers = numFollowers;
        this.tracks = tracks != null ? tracks : new HashSet<>();
    }

}