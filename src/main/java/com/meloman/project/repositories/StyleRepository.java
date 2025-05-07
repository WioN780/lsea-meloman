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
    SELECT s.id,
           s.name
    FROM   Style s
    LEFT   JOIN track_style ts ON ts.style_id = s.id
    LEFT   JOIN album_style als ON als.style_id = s.id
    GROUP  BY s.id, s.name
    ORDER  BY COUNT(*) DESC
    FETCH  FIRST :n ROWS ONLY;
    """, nativeQuery = true
    )
    List<Style> findMostPopular(@Param("n") int n);
}
