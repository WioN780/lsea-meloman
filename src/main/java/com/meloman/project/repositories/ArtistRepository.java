package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;

import com.meloman.project.database_model.Track;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    // Find artists by name
    List<Artist> findByName(String name);

    @Query("""
        SELECT a
        FROM   Track t
        JOIN   t.artists a
        GROUP  BY a
        ORDER  BY COUNT(t) DESC
    """)
    List<Artist> findTopArtists(Pageable pageable);
}