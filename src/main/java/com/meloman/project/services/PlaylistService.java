package com.meloman.project.services;

import com.meloman.project.database_model.Playlist;
import com.meloman.project.database_model.Track;
import com.meloman.project.repositories.PlaylistRepository;
import com.meloman.project.repositories.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;

    public PlaylistService(PlaylistRepository playlistRepository,
                           TrackRepository trackRepository) {
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
    }

    public Playlist create(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist getById(String id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
    }

    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    public Playlist update(Playlist updated) {
        Playlist existing = getById(updated.getId());
        existing.setName(updated.getName());
        existing.setCollaborative(updated.isCollaborative());
        existing.setModifiedAt(updated.getModifiedAt());
        existing.setNumFollowers(updated.getNumFollowers());
        return playlistRepository.save(existing);
    }

    public void delete(String id) {
        if (!playlistRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Playlist with id " + id + " not found");
        }
        playlistRepository.deleteById(id);
    }

    public Playlist addTrackToPlaylist(String playlistId, String trackId) {
        Playlist playlist = getById(playlistId);
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track with id " + trackId + " not found"));
        playlist.getTracks().add(track);
        playlist.setModifiedAt(System.currentTimeMillis());
        return playlistRepository.save(playlist);
    }

    public Playlist removeTrackFromPlaylist(String playlistId, String trackId) {
        Playlist playlist = getById(playlistId);
        playlist.getTracks().removeIf(t -> t.getId().equals(trackId));
        playlist.setModifiedAt(System.currentTimeMillis());
        return playlistRepository.save(playlist);
    }

    public Playlist incrementFollowers(String playlistId) {
        Playlist playlist = getById(playlistId);
        playlist.setNumFollowers(playlist.getNumFollowers() + 1);
        return playlistRepository.save(playlist);
    }
}