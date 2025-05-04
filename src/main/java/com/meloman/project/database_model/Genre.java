package com.meloman.project.database_model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "GENRE")
public class Genre implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "name", length = 255)
    private String name;

    public Genre(){ }

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
