package com.meloman.project.services;

import com.meloman.project.database_model.Genre;
import com.meloman.project.repositories.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre getById(String id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " not found"));
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public List<Genre> getMostPopular(int limit) {
        return genreRepository.findMostPopular(PageRequest.of(0, limit));
    }

    public Genre update(Genre updated) {
        Genre existing = getById(updated.getId());
        existing.setName(updated.getName());
        return genreRepository.save(existing);
    }

    public void delete(String id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre with id " + id + " not found");
        }
        genreRepository.deleteById(id);
    }
}
