package com.meloman.project.services;

import com.meloman.project.database_model.Artist;
import com.meloman.project.repositories.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist create(Artist artist) {
        return artistRepository.save(artist);
    }

    public Artist getById(String id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist with id " + id + " not found"));
    }

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public List<Artist> getByName(String name) {
        return artistRepository.findByName(name);
    }

    public List<Artist> getMostPopular(int N) {
        return artistRepository.findTopArtists(PageRequest.of(0, N));
    }

    public Artist update(Artist updated) {
        Artist existing = getById(updated.getId());
        existing.setName(updated.getName());
        existing.setRealName(updated.getRealName());
        existing.setContactInfo(updated.getContactInfo());
        existing.setUrl(updated.getUrl());
        return artistRepository.save(existing);
    }

    public void delete(String id) {
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Artist with id " + id + " not found");
        }
        artistRepository.deleteById(id);
    }
}