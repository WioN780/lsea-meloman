package com.meloman.project;

import com.meloman.project.database_model.Label;
import com.meloman.project.repositories.LabelRepository;
import com.meloman.project.services.LabelService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LabelServiceJUnitTest {

    @Mock
    private LabelRepository repo;

    @InjectMocks
    private LabelService svc;

    private Label parent;
    private Label sample;

    @Before
    public void setUp() {
        parent = new Label("P001", "Parent Label", null, "parent@info.com", "https://parent.url");
        sample = new Label("L001", "Test Label", "test@label.com", "https://testlabel.com");
        sample.setParentLabel(parent);
    }

    @Test
    public void create_shouldDelegateToRepo() {
        when(repo.save(sample)).thenReturn(sample);

        Label out = svc.create(sample);

        assertSame(sample, out); // because of creating assertEquals wont work (could be false positive)
        verify(repo).save(sample);
    }

    @Test
    public void getById_found() {
        when(repo.findById("L001")).thenReturn(Optional.of(sample));

        Label out = svc.getById("L001");

        assertEquals("Test Label", out.getName()); //check by names
        assertEquals(parent, out.getParentLabel());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_notFound_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        svc.getById("X");
    }

    @Test
    public void getAll_delegatesToRepo() {
        List<Label> all = List.of(sample, parent);
        when(repo.findAll()).thenReturn(all);

        List<Label> out = svc.getAll();

        assertEquals(all, out);
    }

    @Test
    public void getTopLabelsByIncludes_returnsLabels() {
        when(repo.findTopLabels(PageRequest.of(0, 3)))
                .thenReturn(List.of(sample, parent));

        List<Label> result = svc.getTopLabelsByIncludes(3);

        assertEquals(2,       result.size());
        assertSame(sample,  result.get(0));
        assertSame(parent,  result.get(1));
    }

    @Test
    public void update_existing_updatesFieldsAndParent() {
        Label updated = new Label("L001", "New Name", null, "new@info.com", "https://new.url");
        // give it a different parent
        Label newParent = new Label("P002", "Another Parent", null, "p2@info.com", "https://p2.url");
        updated.setParentLabel(newParent);

        when(repo.findById("L001")).thenReturn(Optional.of(sample));
        when(repo.save(any(Label.class))).thenAnswer(inv -> inv.getArgument(0));

        Label out = svc.update(updated);

        assertEquals("New Name", out.getName());
        assertEquals("new@info.com", out.getContactInfo());
        assertEquals("https://new.url", out.getUrl());
        assertSame(newParent, out.getParentLabel());

        verify(repo).save(sample);
    }

    @Test(expected = EntityNotFoundException.class)
    public void update_missing_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());

        Label toUpdate = new Label();
        toUpdate.setId("X");

        svc.update(toUpdate);
    }

    @Test
    public void delete_existing_delegates() {
        when(repo.existsById("L001")).thenReturn(true);

        svc.delete("L001");

        verify(repo).deleteById("L001");
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_missing_throws() {
        when(repo.existsById("X")).thenReturn(false);

        svc.delete("X");
    }

}
