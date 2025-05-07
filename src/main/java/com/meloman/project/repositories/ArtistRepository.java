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
    SELECT a.*
    FROM Artist a
    JOIN Track t ON a.id = t.artist_id
    GROUP BY a.id
    HAVING COUNT(*) = (
        SELECT MAX(track_count)
        FROM (
            SELECT COUNT(*) AS track_count
            FROM Track
            GROUP BY artist_id
        )
    )
    """, nativeQuery = true)
    List<Artist> findWithMostTracks();
}