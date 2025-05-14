package com.meloman.project.repositories;


import com.meloman.project.database_model.Album;

import com.meloman.project.utils.AlbumCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
    @Query("""
      SELECT t.album        AS album,
             COUNT(t)       AS cnt
      FROM   Playlist p
      JOIN   p.tracks t
      WHERE  t.album IS NOT NULL
      GROUP  BY t.album
      ORDER  BY COUNT(t) DESC
    """)
    List<AlbumCount> findAlbumAppearanceCounts(Pageable pageable);
}