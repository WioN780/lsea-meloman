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
    @Query(value = """
    SELECT g.id,
           g.name
    FROM   Genre g
    LEFT   JOIN track_genre tg ON tg.genre_id = g.id
    LEFT   JOIN album_genre ag ON ag.genre_id = g.id
    GROUP  BY g.id, g.name
    ORDER  BY COUNT(*) DESC
    FETCH  FIRST :n ROWS ONLY
    """, nativeQuery = true)
    List<Genre> findMostPopular(@Param("n") int n);


}
