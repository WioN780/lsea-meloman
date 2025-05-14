package com.meloman.project;

import com.meloman.project.database_model.Album;
import com.meloman.project.repositories.AlbumRepository;
import com.meloman.project.services.AlbumService;
import com.meloman.project.utils.AlbumCount;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlbumServiceJUnitTest {

    @Mock
    private AlbumRepository repo;

    @InjectMocks
    private AlbumService svc;

    private Album sample;

    @Before
    public void setUp() {
        sample = new Album(
                "A1",
                "Beer in a bear",
                1959,
                "contact@example.com",
                "http://example.com/kindofblue",
                new HashSet<>(),
                new HashSet<>(),
                null,
                null
        );
    }

    @Test
    public void create_shouldDelegateToRepo() {
        when(repo.save(sample)).thenReturn(sample);
        Album out = svc.create(sample);

        assertSame(sample, out); // because of creating assertEquals wont work (could be false positive)
        verify(repo).save(sample);
    }

    @Test
    public void getById_found() {
        when(repo.findById("A1")).thenReturn(Optional.of(sample));
        Album out = svc.getById("A1");

        assertEquals("Beer in a bear", out.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_notFound_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        svc.getById("X");
    }

    @Test
    public void getAll_delegatesToRepo() {
        List<Album> list = List.of(sample);
        when(repo.findAll()).thenReturn(list);

        assertEquals(list, svc.getAll());
    }

    @Test
    public void sumOfAppearancesOfAlbumContent_returnsCounts() {
        AlbumCount ac = mock(AlbumCount.class);
        when(ac.getAlbum()).thenReturn(sample);
        when(ac.getLong()).thenReturn(42L);

        // Expect PageRequest.of(0, 5)
        when(repo.findAlbumAppearanceCounts(PageRequest.of(0, 5)))
                .thenReturn(List.of(ac));

        List<AlbumCount> result = svc.sumOfAppearancesOfAlbumContent(5);

        assertEquals(1, result.size());
        assertEquals(sample, result.get(0).getAlbum());
        assertEquals(42L, result.get(0).getLong().longValue());
    }

    @Test
    public void update_existing_updatesFields() {
        Album updated = new Album(
                "A1",
                "New Title",
                1960,
                "new@contact.com",
                "http://new.url",
                Set.of(), Set.of(), null, null
        );
        when(repo.findById("A1")).thenReturn(Optional.of(sample));
        when(repo.save(any(Album.class))).thenAnswer(i -> i.getArgument(0));

        Album out = svc.update(updated);

        assertEquals("New Title",    out.getTitle());
        assertEquals(1960,           out.getYear());
        assertEquals("new@contact.com", out.getContactInfo());
        verify(repo).save(sample);
    }

    @Test(expected = EntityNotFoundException.class)
    public void update_missing_throws() {
        when(repo.findById("X")).thenReturn(Optional.empty());
        Album toUpdate = new Album();
        toUpdate.setId("X");

        svc.update(toUpdate);
    }

    @Test
    public void delete_existing_delegates() {
        when(repo.existsById("A1")).thenReturn(true);

        svc.delete("A1");

        verify(repo).deleteById("A1");
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_missing_throws() {
        when(repo.existsById("X")).thenReturn(false);

        svc.delete("X");
    }

}
