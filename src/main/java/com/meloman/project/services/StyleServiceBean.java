package com.meloman.project.services;

import com.meloman.project.database_model.Style;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.util.List;




@Stateless
public class StyleServiceBean {

    @PersistenceContext(unitName="MelomanPU")
    private EntityManager em;

    public Style create(Style style) {
        em.persist(style);
        return style;
    }

    public Style getById(String id) {
        Style s = em.find(Style.class, id);
        if (s == null) {
            throw new EntityNotFoundException("Style with id " + id + " not found");
        }
        return s;
    }

    public List<Style> getAll() {
        return em.createQuery("SELECT s FROM Style s", Style.class)
                .getResultList();
    }

    public List<Style> getMostPopular(int limit) {
        return em.createQuery(
                         "SELECT s " +
                            "FROM Style s " +
                            "LEFT JOIN s.tracks t " +
                            "LEFT JOIN s.albums a " +
                            "GROUP BY s " +
                            "ORDER BY (COUNT(t) + COUNT(a)) DESC",
                        Style.class)
                        .setMaxResults(limit)
                        .getResultList();
    }

    public Style update(Style updated) {
        Style existing = getById(updated.getId());
        existing.setName(updated.getName());
        return em.merge(existing);
    }

    public void delete(String id) {
        Style s = getById(id);
        em.remove(s);
    }
}
