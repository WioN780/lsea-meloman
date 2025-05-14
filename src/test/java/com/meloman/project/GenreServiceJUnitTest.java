package com.meloman.project;

import com.meloman.project.database_model.Genre;
import com.meloman.project.repositories.GenreRepository;
import com.meloman.project.services.GenreService;
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
public class GenreServiceJUnitTest {

    @Mock
    private GenreRepository repo;

    @InjectMocks
    private GenreService svc;

    private Genre sample;

    @Before
    public void setUp() {
        sample = new Genre("G1", "Rock");
    }

    /**
     * Test saving an item
     */
    @Test
    public void create_shouldDelegateToRepo() {
        when(repo.save(sample)).thenReturn(sample);

        Genre out = svc.create(sample);

        assertSame(sample, out); // because of creating assertEquals wont work (could be false positive)
        verify(repo).save(sample);
    }

    /**
     * Test getting an item by id
     */
    @Test
    public void getById_found() {
        when(repo.findById("G1")).thenReturn(Optional.of(sample));

        Genre out = svc.getById("G1");

        assertEquals("Rock", out.getName());
    }

    /**
     * Test correct error throws when getting an item by id that does not exist
     */
    @Test(expected = EntityNotFoundException.class)
    public void getById_notFound_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        svc.getById("X");
    }

    /**
     *Test getting all items
     */
    @Test
    public void getAll_delegatesToRepo() {
        List<Genre> all = List.of(sample);
        when(repo.findAll()).thenReturn(all);

        List<Genre> out = svc.getAll();

        assertEquals(all, out);
    }

    /**
     * Test order of getMostPopular
     */
    @Test
    public void getMostPopular_returnsInOrder() {
        List<Genre> top2 = List.of(sample, new Genre("G2","Jazz"));
        when(repo.findMostPopular(PageRequest.of(0, 2)))
                .thenReturn(top2);

        List<Genre> result = svc.getMostPopular(2);

        assertEquals(2,       result.size());
        assertEquals(sample,  result.get(0));
        assertEquals("Jazz",  result.get(1).getName());
    }

    /**
     * Test updating an item
     */
    @Test
    public void update_existing_changesName() {
        Genre updated = new Genre("G1", "Prog Rock");
        when(repo.findById("G1")).thenReturn(Optional.of(sample));
        when(repo.save(any(Genre.class))).thenAnswer(inv -> inv.getArgument(0));

        Genre out = svc.update(updated);

        assertEquals("Prog Rock", out.getName());
        verify(repo).save(sample);
    }

    /**
     *Test correct error throws when updating an item that does not exist
     */
    @Test(expected = EntityNotFoundException.class)
    public void update_missing_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        Genre toUpdate = new Genre();
        toUpdate.setId("X");

        svc.update(toUpdate);
    }

    /**
     *Test deletion of an item
     */
    @Test
    public void delete_existing_delegates() {
        when(repo.existsById("G1")).thenReturn(true);

        svc.delete("G1");

        verify(repo).deleteById("G1");
    }

    /**
     * Test correct error throws when deleting an item that does not exist
     */
    @Test(expected = EntityNotFoundException.class)
    public void delete_missing_throws() {
        when(repo.existsById("X")).thenReturn(false);

        svc.delete("X");
    }
}
