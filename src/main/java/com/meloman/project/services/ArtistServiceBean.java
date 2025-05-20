package com.meloman.project.services;

import com.meloman.project.database_model.Artist;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;


@Stateless
public class ArtistServiceBean {

    @PersistenceContext(unitName="MelomanPU")
    private EntityManager em;

    public Artist create(Artist artist) {
        em.persist(artist);
        return artist;
    }

    public Artist getById(String id) {
        Artist a = em.find(Artist.class, id);
        if (a == null) {
            throw new EntityNotFoundException("Artist with id " + id + " not found");
        }
        return a;
    }

    public List<Artist> getAll() {
        return em.createQuery("SELECT a FROM Artist a", Artist.class)
                .getResultList();
    }

    public List<Artist> getByName(String name) {
        return em.createQuery(
                        "SELECT a FROM Artist a WHERE a.name = :name", Artist.class)
                .setParameter("name", name)
                .getResultList();
    }


    public List<Artist> getMostPopular(int limit) {
        return em.createQuery(
                        "SELECT a " +
                            "FROM Track t JOIN t.artists a " +
                            "GROUP BY a " +
                            "ORDER BY COUNT(t) DESC",
                        Artist.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public Artist update(Artist updated) {
        Artist existing = getById(updated.getId());
        existing.setName(updated.getName());
        existing.setRealName(updated.getRealName());
        existing.setContactInfo(updated.getContactInfo());
        existing.setUrl(updated.getUrl());
        return em.merge(existing);
    }

    public void delete(String id) {
        Artist a = getById(id);
        em.remove(a);
    }
}
