package com.meloman.project.services;

import com.meloman.project.database_model.Album;
import com.meloman.project.utils.AlbumCountImpl;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;


@Stateless
public class AlbumServiceBean {

    @PersistenceContext(unitName="MelomanPU")
    private EntityManager em;

    public Album create(Album album) {
        em.persist(album);
        return album;
    }

    public List<Album> getAll() {
        return em.createQuery("SELECT a FROM Album a", Album.class)
                .getResultList();
    }

    public Album getById(String id) {
        Album a = em.find(Album.class, id);
        if (a == null) {
            throw new EntityNotFoundException("Album with id " + id + " not found");
        }
        return a;
    }

    public Album update(Album updated) {
        Album existing = getById(updated.getId());
        existing.setTitle(updated.getTitle());
        existing.setContactInfo(updated.getContactInfo());
        existing.setUrl(updated.getUrl());
        existing.setYear(updated.getYear());

        existing.setGenres(updated.getGenres());
        existing.setStyles(updated.getStyles());
        //existing.setArtist(updated.getArtist);
        //existing.setLabel(updated.getLabel));
        return em.merge(existing);
    }

    public void delete(String id) {
        Album a = getById(id);
        em.remove(a);
    }

    public List<AlbumCountImpl> sumOfAppearancesOfAlbumContent(int maxResults) {
        List<Object[]> rows = em.createQuery(
                        "SELECT t.album, COUNT(t) " +
                            "FROM Playlist p JOIN p.tracks t " +
                            "WHERE t.album IS NOT NULL " +
                            "GROUP BY t.album " +
                            "ORDER BY COUNT(t) DESC", Object[].class)
                .setMaxResults(maxResults)
                .getResultList();

        return rows.stream()
                .map(r -> new AlbumCountImpl((Album)r[0], (Long)r[1]))
                .toList();
    }
}
