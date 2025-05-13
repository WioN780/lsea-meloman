package com.meloman.project.services;

import com.meloman.project.database_model.Label;
import com.meloman.project.repositories.LabelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label create(Label label) {
        return labelRepository.save(label);
    }

    public Label getById(String id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label with id " + id + " not found"));
    }

    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    public List<Label> getTopLabelsByIncludes(int N){
        return labelRepository.findTopLabels(PageRequest.of(0, N));
    }

    public Label update(Label updated) {
        Label existing = labelRepository.findById(updated.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot update. Label with id " + updated.getId() + " not found"));

        existing.setName(updated.getName());
        existing.setContactInfo(updated.getContactInfo());
        existing.setUrl(updated.getUrl());
        existing.setParentLabel(updated.getParentLabel());

        return labelRepository.save(existing);
    }

    public void delete(String id) {
        if (!labelRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Label with id " + id + " not found");
        }
        labelRepository.deleteById(id);
    }
}