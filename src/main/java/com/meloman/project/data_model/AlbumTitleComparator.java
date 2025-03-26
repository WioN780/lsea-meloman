package com.meloman.project.data_model;

import java.util.Comparator;

/**
 * A comparator to compare two albums based on their titles.
 * This is used for sorting albums alphabetically by their title in ascending order.
 *
 */

public class AlbumTitleComparator implements Comparator<Album> {

    /**
     * Compares two albums by their title in alphabetical order.
     *
     * @param a1 The first album to be compared.
     * @param a2 The second album to be compared.
     * @return A negative number if the title of the first album is less than the second one,
     *         zero if they are equal, and a positive number if the first album's title is greater.
     */

    @Override
    public int compare(Album a1, Album a2) {
        // Compare by title alphabetically (ascending order)
        return a1.getTitle().compareTo(a2.getTitle());
    }
}
