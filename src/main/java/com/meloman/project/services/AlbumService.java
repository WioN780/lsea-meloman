package com.meloman.project.services;

import com.meloman.project.database_model.Album;
import com.meloman.project.repositories.AlbumRepository;
import com.meloman.project.utils.AlbumCount;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album create(Album album) {
        return albumRepository.save(album);
    }

    public Album getById(String id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album with id " + id + " not found"));
    }

    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    /**
     * Compute the query: "Count sum of number of appearances of every song for an album"
     */
    public List<AlbumCount> sumOfAppearancesOfAlbumContent(int N) {
        return albumRepository.findAlbumAppearanceCounts(PageRequest.of(0, N));
    }

    public Album update(Album updated) {
        Album existing = albumRepository.findById(updated.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot update. Album with id " + updated.getId() + " not found"));

        existing.setTitle(updated.getTitle());
        existing.setContactInfo(updated.getContactInfo());
        existing.setUrl(updated.getUrl());
        existing.setYear(updated.getYear());

        return albumRepository.save(existing);
    }

    public void delete(String id) {
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Album with id " + id + " not found");
        }
        albumRepository.deleteById(id);
    }
}
