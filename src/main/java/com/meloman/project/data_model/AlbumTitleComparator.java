package com.meloman.project.data_model;

import java.util.Comparator;

public class AlbumTitleComparator implements Comparator<Album> {

    @Override
    public int compare(Album a1, Album a2) {
        // Compare by title alphabetically (ascending order)
        return a1.getTitle().compareTo(a2.getTitle());
    }
}
