package com.meloman.project.utils;

import com.meloman.project.database_model.Album;

public class AlbumCountImpl implements AlbumCount {
    private final Album album;
    private final Long count;

    public AlbumCountImpl(Album album, Long count) {
        this.album = album;
        this.count = count;
    }

    @Override
    public Album getAlbum() {
        return album;
    }

    @Override
    public Long getLong() {
        return count;
    }
}
