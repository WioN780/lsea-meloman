package com.meloman.project.repositories;

import com.meloman.project.database_model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    //
}