package com.meloman.project.repositories;

import com.meloman.project.database_model.Style;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
    //

    @Query("""
        SELECT s
        FROM Style s
        LEFT JOIN s.tracks t
        LEFT JOIN s.albums a
        GROUP BY s
        ORDER BY (COUNT(t) + COUNT(a)) DESC
    """)
    List<Style> findMostPopular(Pageable pageable);
}
