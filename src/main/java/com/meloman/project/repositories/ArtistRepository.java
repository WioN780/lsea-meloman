package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;

import com.meloman.project.database_model.Track;
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

    @Query(value = """
    SELECT a.id, a.name, a.contact_info
    FROM   Artist a
    JOIN   artist_track art ON a.id = art.artist_id
    GROUP  BY a.id, a.name, a.contact_info
    ORDER  BY COUNT(*) DESC
    FETCH  FIRST :n ROWS ONLY
    """, nativeQuery = true)
    List<Artist> findTopArtists(@Param("n") int n);
}