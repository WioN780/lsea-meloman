package com.meloman.project.services;

import com.meloman.project.database_model.Style;
import com.meloman.project.repositories.StyleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StyleService {

    private final StyleRepository styleRepository;

    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public Style create(Style style) {
        return styleRepository.save(style);
    }

    public Style getById(String id) {
        return styleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Style with id " + id + " not found"));
    }

    public List<Style> getAll() {
        return styleRepository.findAll();
    }

    public List<Style> getMostPopular(int limit) {
        return styleRepository.findMostPopular(PageRequest.of(0, limit));
    }

    public Style update(Style updated) {
        Style existing = getById(updated.getId());
        existing.setName(updated.getName());
        return styleRepository.save(existing);
    }

    public void delete(String id) {
        if (!styleRepository.existsById(id)) {
            throw new EntityNotFoundException("Style with id " + id + " not found");
        }
        styleRepository.deleteById(id);
    }
}
