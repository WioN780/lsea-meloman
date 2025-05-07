package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;
import com.meloman.project.database_model.Label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {
    //

    // Find artists by name
    List<Label> findByName(String name);

    @Query(value = """
    SELECT a.*
    FROM Label l
    JOIN Track t ON l.id = t.artist_id
    GROUP BY l.id
    HAVING COUNT(*) = (
        SELECT MAX(track_count)
        FROM (
            SELECT COUNT(*) AS track_count
            FROM Track
            GROUP BY label_id
        )
    )
    """, nativeQuery = true)
    List<Label> findWithMostTracks();
}
