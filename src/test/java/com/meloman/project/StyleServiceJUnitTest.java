package com.meloman.project;

import com.meloman.project.database_model.Style;
import com.meloman.project.repositories.StyleRepository;
import com.meloman.project.services.StyleService;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class StyleServiceJUnitTest {

    @Mock
    private StyleRepository repo;

    @InjectMocks
    private StyleService svc;

    private Style sample;

    @Before
    public void setUp() {
        sample = new Style("S1", "Fusion");
    }

    /**
     *Test adding an item
     */
    @Test
    public void create_shouldDelegateToRepo() {
        when(repo.save(sample)).thenReturn(sample);

        Style out = svc.create(sample);

        assertSame(sample, out); // because of creating assertEquals wont work (could be false positive)
        verify(repo).save(sample);
    }

    /**
     *Test getting by id
     */
    @Test
    public void getById_found() {
        when(repo.findById("S1")).thenReturn(Optional.of(sample));

        Style out = svc.getById("S1");

        assertEquals("Fusion", out.getName());
    }

    /**
     *Test correct throw when not finding an item
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_notFound_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        svc.getById("X");
    }

    /**
     *Test get all items
     */
    @Test
    public void getAll_delegatesToRepo() {
        List<Style> all = List.of(sample);
        when(repo.findAll()).thenReturn(all);

        List<Style> out = svc.getAll();

        assertEquals(all, out);
    }

    /**
     * Test order of get most popular
     */
    @Test
    public void getMostPopular_returnsInOrder() {
        Style second = new Style("S2", "Avant-Garde");
        when(repo.findMostPopular(PageRequest.of(0, 3)))
                .thenReturn(List.of(sample, second));

        List<Style> result = svc.getMostPopular(3);

        assertEquals(2,        result.size());
        assertEquals(sample,   result.get(0));
        assertEquals("Avant-Garde", result.get(1).getName());
    }

    /**
     *Test updating items
     */
    @Test
    public void update_existing_changesName() {
        Style updated = new Style("S1", "Jazz-Fusion");
        when(repo.findById("S1")).thenReturn(Optional.of(sample));
        when(repo.save(any(Style.class))).thenAnswer(inv -> inv.getArgument(0));

        Style out = svc.update(updated);

        assertEquals("Jazz-Fusion", out.getName());
        verify(repo).save(sample);
    }

    /**
     * Test correct throw error
     */
    @Test(expected = EntityNotFoundException.class)
    public void update_missing_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        Style toUpdate = new Style();
        toUpdate.setId("X");

        svc.update(toUpdate);
    }

    /**
     * Test deleting item
     */
    @Test
    public void delete_existing_delegates() {
        when(repo.existsById("S1")).thenReturn(true);

        svc.delete("S1");

        verify(repo).deleteById("S1");
    }

    /**
     * Test correct throw when deleting nonexistent item
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete_missing_throws() {
        when(repo.existsById("X")).thenReturn(false);

        svc.delete("X");
    }

}
