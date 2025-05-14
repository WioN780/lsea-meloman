package com.meloman.project.services;

import com.meloman.project.database_model.Track;
import com.meloman.project.repositories.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track create(Track track) {
        return trackRepository.save(track);
    }

    public Track getById(String id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Track with id " + id + " not found"));
    }

    public Track getByIdWithArtists(String id) {
        return trackRepository.findByIdWithArtists(id)
                .orElseThrow(() -> new EntityNotFoundException("Track with id " + id + " not found"));
    }

    public List<Track> getByTitle(String title) {
        return trackRepository.findByTitle(title);
    }

    public List<Track> getByTitleWithArtists(String title) {
        return trackRepository.findByTitleWithArtists(title);
    }

    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    public Track update(Track updated) {
        Track existing = getById(updated.getId());

        existing.setTitle(updated.getTitle());
        existing.setDuration(updated.getDuration());
        existing.setYear(updated.getYear());
        existing.setGenres(updated.getGenres());
        existing.setStyles(updated.getStyles());
        existing.setArtists(updated.getArtists());
        existing.setLabel(updated.getLabel());

        return trackRepository.save(existing);
    }

    public void delete(String id) {
        if (!trackRepository.existsById(id)) {
            throw new EntityNotFoundException("Track with id " + id + " not found");
        }
        trackRepository.deleteById(id);
    }
}
