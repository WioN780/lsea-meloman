package com.meloman.project.repositories;

import com.meloman.project.database_model.Style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
    //

    @Query(
    value =
    """
    SELECT s.*
            FROM Style s
            JOIN song_style ss ON ss.style_id = s.id
            JOIN album_style als ON als.style_id = s.id
            GROUP BY s.id
            HAVING COUNT(*) = (
                SELECT MAX(appearances_count)
                FROM (
                    SELECT COUNT(*) AS appearances_count
                    FROM (
                        SELECT style_id FROM song_style
                        UNION ALL
                        SELECT style_id FROM album_style
                    ) combined_styles
                    GROUP BY style_id
                )
            )
            FETCH FIRST :n ROWS ONLY
    """, nativeQuery = true
    )
    List<Style> findMostPopular(@Param("n") int n);
}
