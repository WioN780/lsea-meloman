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
@Table(name = "ARTIST")
public class Artist implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "real_name", length = 255)
    private String realName;

    @Column(name = "contact_info", length = 500)
    private String contactInfo;

    @Column(name = "url", length = 500)
    private String url;

    public Artist(){ }

    public Artist(String id, String name, String realName, String contactInfo, String url) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.contactInfo = contactInfo;
        this.url = url;
    }
}
