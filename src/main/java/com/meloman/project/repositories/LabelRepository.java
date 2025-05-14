package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;
import com.meloman.project.database_model.Label;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {
    //

    // Find artists by name
    List<Label> findByName(String name);

    @Query("""
        SELECT l
        FROM   Track t
        JOIN   t.label l
        GROUP  BY l
        ORDER  BY COUNT(t) DESC
    """)
    List<Label> findTopLabels(Pageable pageable);
}
