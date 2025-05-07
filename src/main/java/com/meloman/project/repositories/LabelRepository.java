package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;
import com.meloman.project.database_model.Label;

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

    @Query(value = """
    SELECT l.id,
           l.name,
           l.parent_label_id,
           l.contact_info,
           l.url
    FROM   Label  l
    JOIN   Track  t ON l.id = t.label_id
    GROUP  BY l.id, l.name, l.parent_label_id, l.contact_info, l.url
    ORDER  BY COUNT(*) DESC
    FETCH  FIRST :n ROWS ONLY
    """, nativeQuery = true)
    List<Label> findTopLabels(@Param("n") int n);
}
