package com.meloman.project.repositories;

import com.meloman.project.database_model.Genre;

import com.meloman.project.database_model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {
    //
    @Query(
    value =
            """
            SELECT g.*
                    FROM Genre g
                    JOIN song_genre ss ON sg.genre_id = g.id
                    JOIN album_genre alg ON alg.genre_id = s.id
                    GROUP BY s.id
                    HAVING COUNT(*) = (
                        SELECT MAX(appearances_count)
                        FROM (
                            SELECT COUNT(*) AS appearances_count
                            FROM (
                                SELECT genre_id FROM song_genre
                                UNION ALL
                                SELECT genre_id FROM album_genre
                            ) combined_genres
                            GROUP BY genre_id
                        )
                    )
                    FETCH FIRST :n ROWS ONLY
            """, nativeQuery = true
    )
    List<Genre> findMostPopular(@Param("n") int n);

}
