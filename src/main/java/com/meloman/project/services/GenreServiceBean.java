package com.meloman.project.services;

import com.meloman.project.database_model.Genre;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;


@Stateless
public class GenreServiceBean {

    @PersistenceContext(unitName="MelomanPU")
    private EntityManager em;

    public Genre create(Genre genre) {
        if (genre.getId() == null) {
            genre.setId(UUID.randomUUID().toString());
        }

        em.persist(genre);
        return genre;
    }

    public Genre getById(String id) {
        Genre g = em.find(Genre.class, id);
        if (g == null) {
            throw new EntityNotFoundException("Genre with id " + id + " not found");
        }
        return g;
    }

    public List<Genre> getAll() {
        return em.createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();
    }

    public List<Genre> getMostPopular(int limit) {
        return em.createQuery(
                         "SELECT g " +
                            "FROM Genre g " +
                            "LEFT JOIN g.tracks t " +
                            "LEFT JOIN g.albums a " +
                            "GROUP BY g " +
                            "ORDER BY (COUNT(t) + COUNT(a)) DESC",
                        Genre.class)
                        .setMaxResults(limit)
                        .getResultList();
    }

    public Genre update(Genre updated) {
        Genre existing = getById(updated.getId());
        existing.setName(updated.getName());
        return em.merge(existing);
    }

    public void delete(String id) {
        Genre g = getById(id);
        em.remove(g);
    }
}
