package com.meloman.project.repositories;

import com.meloman.project.database_model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    // Find tracks by title
    List<Track> findByTitle(String title);

    // Find tracks by title with eagerly loaded artists
    @Query("SELECT DISTINCT t FROM Track t JOIN FETCH t.artists WHERE t.title = :title")
    List<Track> findByTitleWithArtists(@Param("title") String title);

    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.artists WHERE t.id = :id")
    Optional<Track> findByIdWithArtists(@Param("id") String id);

}