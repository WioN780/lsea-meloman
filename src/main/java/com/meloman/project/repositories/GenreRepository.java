package com.meloman.project.repositories;

import com.meloman.project.database_model.Genre;

import com.meloman.project.database_model.Style;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {
    //
    @Query("""
        SELECT g
        FROM Genre g
        LEFT JOIN g.tracks t
        LEFT JOIN g.albums a
        GROUP BY g
        ORDER BY (COUNT(t) + COUNT(a)) DESC
    """)
    List<Genre> findMostPopular(Pageable pageable);
}
