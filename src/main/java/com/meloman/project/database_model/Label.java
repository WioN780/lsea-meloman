package com.meloman.project.database_model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "LABEL")
public class Label implements Serializable {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "name", length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_label_id", referencedColumnName = "id")
    private Label parentLabel;

    @Column(name = "contact_info", length = 500)
    private String contactInfo;

    @Column(name = "url", length = 500)
    private String url;

    public Label(){ }

    public Label(String id, String name, String contactInfo, String url) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.url = url;
    }

    public Label(String id, String name, Label parentLabel, String contactInfo, String url) {
        this.id = id;
        this.name = name;
        this.parentLabel = parentLabel;
        this.contactInfo = contactInfo;
        this.url = url;
    }
}