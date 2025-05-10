package com.meloman.project.tests;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.meloman.project.database_model.*;
import com.meloman.project.repositories.*;

public class DatabaseModelTests {

    // Mock repositories
    private LabelRepository labelRepository;
    private ArtistRepository artistRepository;
    private TrackRepository trackRepository;
    private AlbumRepository albumRepository;
    private GenreRepository genreRepository;
    private StyleRepository styleRepository;
    private PlaylistRepository playlistRepository;


    public void testInsertLabel_ShouldSuccessfullyInsertLabel() {
        // Arrange
        Label label = new Label();
        label.setId("L001");
        label.setName("Test Label");
        label.setContactInfo("test@label.com");
        label.setUrl("https://testlabel.com");

        // Act
        Label savedLabel = labelRepository.save(label);

        // Assert
        assert savedLabel != null : "Label was not saved";
        assert savedLabel.getId().equals("L001") : "Label ID does not match";
        assert savedLabel.getName().equals("Test Label") : "Label name does not match";
        assert savedLabel.getContactInfo().equals("test@label.com") : "Label contact info does not match";
        assert savedLabel.getUrl().equals("https://testlabel.com") : "Label URL does not match";
    }

    public void testUpdateLabel_ShouldSuccessfullyUpdateLabel() {
        // Arrange
        Label label = new Label();
        label.setId("L002");
        label.setName("Original Label");
        label.setContactInfo("original@label.com");
        label.setUrl("https://originallabel.com");

        Label savedLabel = labelRepository.save(label);

        // Act
        savedLabel.setName("Updated Label");
        savedLabel.setContactInfo("updated@label.com");
        savedLabel.setUrl("https://updatedlabel.com");
        Label updatedLabel = labelRepository.save(savedLabel);

        // Assert
        assert updatedLabel != null : "Updated label was not saved";
        assert updatedLabel.getId().equals("L002") : "Label ID does not match";
        assert updatedLabel.getName().equals("Updated Label") : "Label name was not updated";
        assert updatedLabel.getContactInfo().equals("updated@label.com") : "Label contact info was not updated";
        assert updatedLabel.getUrl().equals("https://updatedlabel.com") : "Label URL was not updated";
    }

    public void testDeleteLabel_ShouldSuccessfullyDeleteLabel() {
        // Arrange
        Label label = new Label();
        label.setId("L003");
        label.setName("Delete Test Label");
        label.setContactInfo("delete@label.com");
        label.setUrl("https://deletelabel.com");

        Label savedLabel = labelRepository.save(label);
        assert labelRepository.findById("L003").isPresent() : "Label was not saved before deletion test";

        // Act
        labelRepository.delete(savedLabel);

        // Assert
        assert !labelRepository.findById("L003").isPresent() : "Label was not deleted";
    }

    public void testFindLabelByName_ShouldReturnMatchingLabels() {
        // Arrange
        Label label1 = new Label();
        label1.setId("L004");
        label1.setName("Find Test Label");
        label1.setContactInfo("find1@label.com");
        labelRepository.save(label1);

        Label label2 = new Label();
        label2.setId("L005");
        label2.setName("Find Test Label");
        label2.setContactInfo("find2@label.com");
        labelRepository.save(label2);

        // Act
        List<Label> foundLabels = labelRepository.findByName("Find Test Label");

        // Assert
        assert foundLabels.size() == 2 : "Did not find the expected number of labels";
        assert foundLabels.stream().anyMatch(l -> l.getId().equals("L004")) : "First label not found";
        assert foundLabels.stream().anyMatch(l -> l.getId().equals("L005")) : "Second label not found";
    }

    public void testFindTopLabels_ShouldReturnTopLabels() {
        // Arrange
        Label label1 = new Label("L006", "Top Label 1", "contact1@label.com", "https://toplabel1.com");
        Label label2 = new Label("L007", "Top Label 2", "contact2@label.com", "https://toplabel2.com");
        labelRepository.save(label1);
        labelRepository.save(label2);

        // Act
        List<Label> topLabels = labelRepository.findTopLabels(5);

        // Assert
        assert topLabels != null : "Top labels query failed";
        assert topLabels.size() > 0 : "No top labels returned";
    }

    public void testInsertLabelWithParent_ShouldSuccessfullyInsertLabelWithParentReference() {
        // Arrange
        Label parentLabel = new Label("PL001", "Parent Label", "parent@label.com", "https://parentlabel.com");
        Label childLabel = new Label();
        childLabel.setId("CL001");
        childLabel.setName("Child Label");
        childLabel.setParentLabel(parentLabel);
        childLabel.setContactInfo("child@label.com");

        labelRepository.save(parentLabel);

        // Act
        Label savedChildLabel = labelRepository.save(childLabel);

        // Assert
        assert savedChildLabel != null : "Child label was not saved";
        assert savedChildLabel.getParentLabel() != null : "Parent label reference is null";
        assert savedChildLabel.getParentLabel().getId().equals("PL001") : "Parent label ID does not match";
    }

    // Artist tests

    public void testInsertArtist_ShouldSuccessfullyInsertArtist() {
        // Arrange
        Artist artist = new Artist();
        artist.setId("A001");
        artist.setName("Test Artist");
        artist.setRealName("Real Name");
        artist.setContactInfo("artist@test.com");
        artist.setUrl("https://testartist.com");

        // Act
        Artist savedArtist = artistRepository.save(artist);

        // Assert
        assert savedArtist != null : "Artist was not saved";
        assert savedArtist.getId().equals("A001") : "Artist ID does not match";
        assert savedArtist.getName().equals("Test Artist") : "Artist name does not match";
        assert savedArtist.getRealName().equals("Real Name") : "Artist real name does not match";
        assert savedArtist.getContactInfo().equals("artist@test.com") : "Artist contact info does not match";
        assert savedArtist.getUrl().equals("https://testartist.com") : "Artist URL does not match";
    }

    public void testUpdateArtist_ShouldSuccessfullyUpdateArtist() {
        // Arrange
        Artist artist = new Artist();
        artist.setId("A002");
        artist.setName("Original Artist");
        artist.setRealName("Original Real Name");
        artist.setContactInfo("original@artist.com");

        Artist savedArtist = artistRepository.save(artist);

        // Act
        savedArtist.setName("Updated Artist");
        savedArtist.setRealName("Updated Real Name");
        savedArtist.setContactInfo("updated@artist.com");
        Artist updatedArtist = artistRepository.save(savedArtist);

        // Assert
        assert updatedArtist != null : "Updated artist was not saved";
        assert updatedArtist.getId().equals("A002") : "Artist ID does not match";
        assert updatedArtist.getName().equals("Updated Artist") : "Artist name was not updated";
        assert updatedArtist.getRealName().equals("Updated Real Name") : "Artist real name was not updated";
        assert updatedArtist.getContactInfo().equals("updated@artist.com") : "Artist contact info was not updated";
    }

    public void testDeleteArtist_ShouldSuccessfullyDeleteArtist() {
        // Arrange
        Artist artist = new Artist();
        artist.setId("A003");
        artist.setName("Delete Test Artist");

        Artist savedArtist = artistRepository.save(artist);
        assert artistRepository.findById("A003").isPresent() : "Artist was not saved before deletion test";

        // Act
        artistRepository.delete(savedArtist);

        // Assert
        assert !artistRepository.findById("A003").isPresent() : "Artist was not deleted";
    }

    public void testFindArtistsByName_ShouldReturnMatchingArtists() {
        // Arrange
        Artist artist1 = new Artist();
        artist1.setId("A004");
        artist1.setName("Find Test Artist");
        artistRepository.save(artist1);

        Artist artist2 = new Artist();
        artist2.setId("A005");
        artist2.setName("Find Test Artist");
        artistRepository.save(artist2);

        // Act
        List<Artist> foundArtists = artistRepository.findByName("Find Test Artist");

        // Assert
        assert foundArtists.size() == 2 : "Did not find the expected number of artists";
        assert foundArtists.stream().anyMatch(a -> a.getId().equals("A004")) : "First artist not found";
        assert foundArtists.stream().anyMatch(a -> a.getId().equals("A005")) : "Second artist not found";
    }

    public void testFindTopArtists_ShouldReturnTopArtists() {
        // Arrange
        Artist artist1 = new Artist("A006", "Top Artist 1", "Real Name 1", "contact1@artist.com", "https://topartist1.com");
        Artist artist2 = new Artist("A007", "Top Artist 2", "Real Name 2", "contact2@artist.com", "https://topartist2.com");
        artistRepository.save(artist1);
        artistRepository.save(artist2);

        // Act
        List<Artist> topArtists = artistRepository.findTopArtists(5);

        // Assert
        assert topArtists != null : "Top artists query failed";
        assert topArtists.size() > 0 : "No top artists returned";
    }

    //Track Tests

    public void testInsertTrack_ShouldSuccessfullyInsertTrack() {
        // Arrange
        Label label = new Label("L101", "Track Label", "label@track.com", "https://tracklabel.com");
        labelRepository.save(label);

        Track track = new Track();
        track.setId("T001");
        track.setTitle("Test Track");
        track.setDuration(180.0);
        track.setYear(2023);
        track.setLabel(label);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());

        // Act
        Track savedTrack = trackRepository.save(track);

        // Assert
        assert savedTrack != null : "Track was not saved";
        assert savedTrack.getId().equals("T001") : "Track ID does not match";
        assert savedTrack.getTitle().equals("Test Track") : "Track title does not match";
        assert savedTrack.getDuration() == 180.0 : "Track duration does not match";
        assert savedTrack.getYear() == 2023 : "Track year does not match";
        assert savedTrack.getLabel() != null : "Track label is null";
        assert savedTrack.getLabel().getId().equals("L101") : "Track label ID does not match";
    }

    public void testUpdateTrack_ShouldSuccessfullyUpdateTrack() {
        // Arrange
        Label label1 = new Label("L102", "Original Track Label", "original@track.com", "https://originaltracklabel.com");
        Label label2 = new Label("L103", "Updated Track Label", "updated@track.com", "https://updatedtracklabel.com");
        labelRepository.save(label1);
        labelRepository.save(label2);

        Track track = new Track();
        track.setId("T002");
        track.setTitle("Original Track");
        track.setDuration(200.0);
        track.setYear(2022);
        track.setLabel(label1);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());

        Track savedTrack = trackRepository.save(track);

        // Act
        savedTrack.setTitle("Updated Track");
        savedTrack.setDuration(210.0);
        savedTrack.setYear(2023);
        savedTrack.setLabel(label2);
        Track updatedTrack = trackRepository.save(savedTrack);

        // Assert
        assert updatedTrack != null : "Updated track was not saved";
        assert updatedTrack.getId().equals("T002") : "Track ID does not match";
        assert updatedTrack.getTitle().equals("Updated Track") : "Track title was not updated";
        assert updatedTrack.getDuration() == 210.0 : "Track duration was not updated";
        assert updatedTrack.getYear() == 2023 : "Track year was not updated";
        assert updatedTrack.getLabel().getId().equals("L103") : "Track label was not updated";
    }

    public void testDeleteTrack_ShouldSuccessfullyDeleteTrack() {
        // Arrange
        Label label = new Label("L104", "Delete Track Label", "delete@track.com", "https://deletetracklabel.com");
        labelRepository.save(label);

        Track track = new Track();
        track.setId("T003");
        track.setTitle("Delete Test Track");
        track.setLabel(label);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());

        Track savedTrack = trackRepository.save(track);
        assert trackRepository.findById("T003").isPresent() : "Track was not saved before deletion test";

        // Act
        trackRepository.delete(savedTrack);

        // Assert
        assert !trackRepository.findById("T003").isPresent() : "Track was not deleted";
    }

    public void testFindTracksByTitle_ShouldReturnMatchingTracks() {
        // Arrange
        Label label = new Label("L105", "Find Track Label", "find@track.com", "https://findtracklabel.com");
        labelRepository.save(label);

        Track track1 = new Track();
        track1.setId("T004");
        track1.setTitle("Find Test Track");
        track1.setLabel(label);
        track1.setArtists(new HashSet<>());
        track1.setGenres(new HashSet<>());
        track1.setStyles(new HashSet<>());
        trackRepository.save(track1);

        Track track2 = new Track();
        track2.setId("T005");
        track2.setTitle("Find Test Track");
        track2.setLabel(label);
        track2.setArtists(new HashSet<>());
        track2.setGenres(new HashSet<>());
        track2.setStyles(new HashSet<>());
        trackRepository.save(track2);

        // Act
        List<Track> foundTracks = trackRepository.findByTitle("Find Test Track");

        // Assert
        assert foundTracks.size() == 2 : "Did not find the expected number of tracks";
        assert foundTracks.stream().anyMatch(t -> t.getId().equals("T004")) : "First track not found";
        assert foundTracks.stream().anyMatch(t -> t.getId().equals("T005")) : "Second track not found";
    }

    public void testAddArtistToTrack_ShouldSuccessfullyAddArtistToTrack() {
        // Arrange
        Label label = new Label("L106", "Artist Track Label", "artist@track.com", "https://artisttracklabel.com");
        labelRepository.save(label);

        Artist artist = new Artist("A101", "Track Artist", "Real Track Artist", "artist@track.com", "https://trackartist.com");
        artistRepository.save(artist);

        Track track = new Track();
        track.setId("T006");
        track.setTitle("Artist Test Track");
        track.setLabel(label);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());
        Track savedTrack = trackRepository.save(track);

        // Act
        Set<Artist> artists = new HashSet<>();
        artists.add(artist);
        savedTrack.setArtists(artists);
        Track updatedTrack = trackRepository.save(savedTrack);

        // Assert
        assert updatedTrack.getArtists().size() == 1 : "Artist was not added to track";
        assert updatedTrack.getArtists().stream().anyMatch(a -> a.getId().equals("A101")) : "Specific artist not found in track's artists";
    }

    public void testAddGenreToTrack_ShouldSuccessfullyAddGenreToTrack() {
        // Arrange
        Label label = new Label("L107", "Genre Track Label", "genre@track.com", "https://genretracklabel.com");
        labelRepository.save(label);

        Genre genre = new Genre("G101", "Test Genre");
        genreRepository.save(genre);

        Track track = new Track();
        track.setId("T007");
        track.setTitle("Genre Test Track");
        track.setLabel(label);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());
        Track savedTrack = trackRepository.save(track);

        // Act
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        savedTrack.setGenres(genres);
        Track updatedTrack = trackRepository.save(savedTrack);

        // Assert
        assert updatedTrack.getGenres().size() == 1 : "Genre was not added to track";
        assert updatedTrack.getGenres().stream().anyMatch(g -> g.getId().equals("G101")) : "Specific genre not found in track's genres";
    }

    public void testAddStyleToTrack_ShouldSuccessfullyAddStyleToTrack() {
        // Arrange
        Label label = new Label("L108", "Style Track Label", "style@track.com", "https://styletracklabel.com");
        labelRepository.save(label);

        Style style = new Style("S101", "Test Style");
        styleRepository.save(style);

        Track track = new Track();
        track.setId("T008");
        track.setTitle("Style Test Track");
        track.setLabel(label);
        track.setArtists(new HashSet<>());
        track.setGenres(new HashSet<>());
        track.setStyles(new HashSet<>());
        Track savedTrack = trackRepository.save(track);

        // Act
        Set<Style> styles = new HashSet<>();
        styles.add(style);
        savedTrack.setStyles(styles);
        Track updatedTrack = trackRepository.save(savedTrack);

        // Assert
        assert updatedTrack.getStyles().size() == 1 : "Style was not added to track";
        assert updatedTrack.getStyles().stream().anyMatch(s -> s.getId().equals("S101")) : "Specific style not found in track's styles";
    }

    //Album Tests

    public void testInsertAlbum_ShouldSuccessfullyInsertAlbum() {
        // Arrange
        Label label = new Label("L201", "Album Label", "label@album.com", "https://albumlabel.com");
        labelRepository.save(label);

        Artist artist = new Artist("A201", "Album Artist", "Real Album Artist", "artist@album.com", "https://albumartist.com");
        artistRepository.save(artist);

        Album album = new Album();
        album.setId("AL001");
        album.setTitle("Test Album");
        album.setYear(2023);
        album.setContactInfo("album@test.com");
        album.setUrl("https://testalbum.com");
        album.setGenres(new HashSet<>());
        album.setStyles(new HashSet<>());
        album.setArtist(artist);
        album.setLabel(label);

        // Act
        Album savedAlbum = albumRepository.save(album);

        // Assert
        assert savedAlbum != null : "Album was not saved";
        assert savedAlbum.getId().equals("AL001") : "Album ID does not match";
        assert savedAlbum.getTitle().equals("Test Album") : "Album title does not match";
        assert savedAlbum.getYear() == 2023 : "Album year does not match";
        assert savedAlbum.getContactInfo().equals("album@test.com") : "Album contact info does not match";
        assert savedAlbum.getUrl().equals("https://testalbum.com") : "Album URL does not match";
        assert savedAlbum.getArtist().getId().equals("A201") : "Album artist ID does not match";
        assert savedAlbum.getLabel().getId().equals("L201") : "Album label ID does not match";
    }

    public void testUpdateAlbum_ShouldSuccessfullyUpdateAlbum() {
        // Arrange
        Label label1 = new Label("L202", "Original Album Label", "original@album.com", "https://originalalbum.com");
        Label label2 = new Label("L203", "Updated Album Label", "updated@album.com", "https://updatedalbum.com");
        labelRepository.save(label1);
        labelRepository.save(label2);

        Artist artist1 = new Artist("A202", "Original Album Artist", "Original Real Artist", "original@artist.com", "https://originalartist.com");
        Artist artist2 = new Artist("A203", "Updated Album Artist", "Updated Real Artist", "updated@artist.com", "https://updatedartist.com");
        artistRepository.save(artist1);
        artistRepository.save(artist2);

        Album album = new Album();
        album.setId("AL002");
        album.setTitle("Original Album");
        album.setYear(2022);
        album.setContactInfo("original@album.com");
        album.setUrl("https://originalalbum.com");
        album.setGenres(new HashSet<>());
        album.setStyles(new HashSet<>());
        album.setArtist(artist1);
        album.setLabel(label1);

        Album savedAlbum = albumRepository.save(album);

        // Act
        savedAlbum.setTitle("Updated Album");
        savedAlbum.setYear(2023);
        savedAlbum.setContactInfo("updated@album.com");
        savedAlbum.setUrl("https://updatedalbum.com");
        savedAlbum.setArtist(artist2);
        savedAlbum.setLabel(label2);
        Album updatedAlbum = albumRepository.save(savedAlbum);

        // Assert
        assert updatedAlbum != null : "Updated album was not saved";
        assert updatedAlbum.getId().equals("AL002") : "Album ID does not match";
        assert updatedAlbum.getTitle().equals("Updated Album") : "Album title was not updated";
        assert updatedAlbum.getYear() == 2023 : "Album year was not updated";
        assert updatedAlbum.getContactInfo().equals("updated@album.com") : "Album contact info was not updated";
        assert updatedAlbum.getUrl().equals("https://updatedalbum.com") : "Album URL was not updated";
        assert updatedAlbum.getArtist().getId().equals("A203") : "Album artist was not updated";
        assert updatedAlbum.getLabel().getId().equals("L203") : "Album label was not updated";
    }

    public void testDeleteAlbum_ShouldSuccessfullyDeleteAlbum() {
        // Arrange
        Label label = new Label("L204", "Delete Album Label", "delete@album.com", "https://deletealbum.com");
        labelRepository.save(label);

        Artist artist = new Artist("A204", "Delete Album Artist", "Delete Real Artist", "delete@artist.com", "https://deleteartist.com");
        artistRepository.save(artist);

        Album album = new Album();
        album.setId("AL003");
        album.setTitle("Delete Test Album");
        album.setArtist(artist);
        album.setLabel(label);
        album.setGenres(new HashSet<>());
        album.setStyles(new HashSet<>());

        Album savedAlbum = albumRepository.save(album);
        assert albumRepository.findById("AL003").isPresent() : "Album was not saved before deletion test";

        // Act
        albumRepository.delete(savedAlbum);

        // Assert
        assert !albumRepository.findById("AL003").isPresent() : "Album was not deleted";
    }

    public void testAddGenreToAlbum_ShouldSuccessfullyAddGenreToAlbum() {
        // Arrange
        Label label = new Label("L205", "Genre Album Label", "genre@album.com", "https://genrealbum.com");
        labelRepository.save(label);

        Artist artist = new Artist("A205", "Genre Album Artist", "Genre Real Artist", "genre@artist.com", "https://genreartist.com");
        artistRepository.save(artist);

        Genre genre = new Genre("G201", "Album Test Genre");
        genreRepository.save(genre);

        Album album = new Album();
        album.setId("AL004");
        album.setTitle("Genre Test Album");
        album.setArtist(artist);
        album.setLabel(label);
        album.setGenres(new HashSet<>());
        album.setStyles(new HashSet<>());
        Album savedAlbum = albumRepository.save(album);

        // Act
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        savedAlbum.setGenres(genres);
        Album updatedAlbum = albumRepository.save(savedAlbum);

        // Assert
        assert updatedAlbum.getGenres().size() == 1 : "Genre was not added to album";
        assert updatedAlbum.getGenres().stream().anyMatch(g -> g.getId().equals("G201")) : "Specific genre not found in album's genres";
    }

    public void testAddStyleToAlbum_ShouldSuccessfullyAddStyleToAlbum() {
        // Arrange
        Label label = new Label("L206", "Style Album Label", "style@album.com", "https://stylealbum.com");
        labelRepository.save(label);

        Artist artist = new Artist("A206", "Style Album Artist", "Style Real Artist", "style@artist.com", "https://styleartist.com");
        artistRepository.save(artist);

        Style style = new Style("S201", "Album Test Style");
        styleRepository.save(style);

        Album album = new Album();
        album.setId("AL005");
        album.setTitle("Style Test Album");
        album.setArtist(artist);
        album.setLabel(label);
        album.setGenres(new HashSet<>());
        album.setStyles(new HashSet<>());
        Album savedAlbum = albumRepository.save(album);

        // Act
        Set<Style> styles = new HashSet<>();
        styles.add(style);
        savedAlbum.setStyles(styles);
        Album updatedAlbum = albumRepository.save(savedAlbum);

        // Assert
        assert updatedAlbum.getStyles().size() == 1 : "Style was not added to album";
        assert updatedAlbum.getStyles().stream().anyMatch(s -> s.getId().equals("S201")) : "Specific style not found in album's styles";
    }

    //Genre tests

    public void testInsertGenre_ShouldSuccessfullyInsertGenre() {
        // Arrange
        Genre genre = new Genre();
        genre.setId("G001");
        genre.setName("Test Genre");

        // Act
        Genre savedGenre = genreRepository.save(genre);

        // Assert
        assert savedGenre != null : "Genre was not saved";
        assert savedGenre.getId().equals("G001") : "Genre ID does not match";
        assert savedGenre.getName().equals("Test Genre") : "Genre name does not match";
    }

    public void testUpdateGenre_ShouldSuccessfullyUpdateGenre() {
        // Arrange
        Genre genre = new Genre();
        genre.setId("G002");
        genre.setName("Original Genre");

        Genre savedGenre = genreRepository.save(genre);

        // Act
        savedGenre.setName("Updated Genre");
        Genre updatedGenre = genreRepository.save(savedGenre);

        // Assert
        assert updatedGenre != null : "Updated genre was not saved";
        assert updatedGenre.getId().equals("G002") : "Genre ID does not match";
        assert updatedGenre.getName().equals("Updated Genre") : "Genre name was not updated";
    }

    public void testDeleteGenre_ShouldSuccessfullyDeleteGenre() {
        // Arrange
        Genre genre = new Genre();
        genre.setId("G003");
        genre.setName("Delete Test Genre");

        Genre savedGenre = genreRepository.save(genre);
        assert genreRepository.findById("G003").isPresent() : "Genre was not saved before deletion test";

        // Act
        genreRepository.delete(savedGenre);

        // Assert
        assert !genreRepository.findById("G003").isPresent() : "Genre was not deleted";
    }

    public void testFindMostPopularGenres_ShouldReturnMostPopularGenres() {
        // Arrange
        Genre genre1 = new Genre("G004", "Popular Genre 1");
        Genre genre2 = new Genre("G005", "Popular Genre 2");
        Genre genre3 = new Genre("G006", "Popular Genre 3");
        genreRepository.save(genre1);
        genreRepository.save(genre2);
        genreRepository.save(genre3);

        // Act
        List<Genre> mostPopularGenres = genreRepository.findMostPopular(2);

        // Assert
        assert mostPopularGenres != null : "Most popular genres query failed";
        assert mostPopularGenres.size() <= 2 : "More genres returned than requested limit";
        assert mostPopularGenres.size() > 0 : "No popular genres returned";
    }
    // Style tests

    public void testInsertStyle_ShouldSuccessfullyInsertStyle() {
        // Arrange
        Style style = new Style("style1", "Rock");

        // Act
        styleRepository.save(style);

        // Assert
        Optional<Style> savedStyle = styleRepository.findById("style1");
        assert savedStyle.isPresent();
        assert savedStyle.get().getName().equals("Rock");
    }

    public void testUpdateStyle_ShouldSuccessfullyUpdateStyle() {
        // Arrange
        Style style = new Style("style1", "Rock");
        styleRepository.save(style);

        // Act
        style.setName("Pop Rock");
        styleRepository.save(style);

        // Assert
        Optional<Style> updatedStyle = styleRepository.findById("style1");
        assert updatedStyle.isPresent();
        assert updatedStyle.get().getName().equals("Pop Rock");
    }

    public void testDeleteStyle_ShouldSuccessfullyDeleteStyle() {
        // Arrange
        Style style = new Style("style1", "Rock");
        styleRepository.save(style);

        // Act
        styleRepository.deleteById("style1");

        // Assert
        Optional<Style> deletedStyle = styleRepository.findById("style1");
        assert deletedStyle.isEmpty();
    }

    public void testFindMostPopularStyles_ShouldReturnMostPopularStyles() {
        // Arrange
        Style style1 = new Style("style1", "Rock");
        Style style2 = new Style("style2", "Pop");
        Style style3 = new Style("style3", "Electronic");
        styleRepository.saveAll(List.of(style1, style2, style3));

        // Act
        List<Style> popularStyles = styleRepository.findMostPopular(2);

        // Assert
        assert popularStyles.size() <= 2;
    }

// Playlist Tests

    public void testInsertPlaylist_ShouldSuccessfullyInsertPlaylist() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 0);

        // Act
        playlistRepository.save(playlist);

        // Assert
        Optional<Playlist> savedPlaylist = playlistRepository.findById("playlist1");
        assert savedPlaylist.isPresent();
        assert savedPlaylist.get().getName().equals("My Favorites");
    }

    public void testUpdatePlaylist_ShouldSuccessfullyUpdatePlaylist() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 0);
        playlistRepository.save(playlist);

        // Act
        playlist.setName("Updated Playlist");
        playlist.setCollaborative(true);
        playlist.setNumFollowers(10);
        playlistRepository.save(playlist);

        // Assert
        Optional<Playlist> updatedPlaylist = playlistRepository.findById("playlist1");
        assert updatedPlaylist.isPresent();
        assert updatedPlaylist.get().getName().equals("Updated Playlist");
        assert updatedPlaylist.get().isCollaborative();
        assert updatedPlaylist.get().getNumFollowers() == 10;
    }

    public void testDeletePlaylist_ShouldSuccessfullyDeletePlaylist() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 0);
        playlistRepository.save(playlist);

        // Act
        playlistRepository.deleteById("playlist1");

        // Assert
        Optional<Playlist> deletedPlaylist = playlistRepository.findById("playlist1");
        assert deletedPlaylist.isEmpty();
    }

    public void testAddTracksToPlaylist_ShouldSuccessfullyAddTracksToPlaylist() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 0);
        playlistRepository.save(playlist);

        Label label = new Label("label1", "Test Label", "contact info", "url");
        labelRepository.save(label);

        Track track1 = new Track("track1", "Track 1", 2022, new HashSet<>(), new HashSet<>(), new HashSet<>(), label);
        Track track2 = new Track("track2", "Track 2", 2023, new HashSet<>(), new HashSet<>(), new HashSet<>(), label);
        trackRepository.saveAll(List.of(track1, track2));

        // Act
        playlist.getTracks().add(track1);
        playlist.getTracks().add(track2);
        playlistRepository.save(playlist);

        // Assert
        Optional<Playlist> updatedPlaylist = playlistRepository.findById("playlist1");
        assert updatedPlaylist.isPresent();
        assert updatedPlaylist.get().getTracks().size() == 2;
        assert updatedPlaylist.get().getTracks().contains(track1);
        assert updatedPlaylist.get().getTracks().contains(track2);
    }

    public void testRemoveTrackFromPlaylist_ShouldSuccessfullyRemoveTrackFromPlaylist() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 0);

        Label label = new Label("label1", "Test Label", "contact info", "url");
        labelRepository.save(label);

        Track track1 = new Track("track1", "Track 1", 2022, new HashSet<>(), new HashSet<>(), new HashSet<>(), label);
        Track track2 = new Track("track2", "Track 2", 2023, new HashSet<>(), new HashSet<>(), new HashSet<>(), label);
        trackRepository.saveAll(List.of(track1, track2));

        playlist.getTracks().add(track1);
        playlist.getTracks().add(track2);
        playlistRepository.save(playlist);

        // Act
        playlist.getTracks().remove(track1);
        playlistRepository.save(playlist);

        // Assert
        Optional<Playlist> updatedPlaylist = playlistRepository.findById("playlist1");
        assert updatedPlaylist.isPresent();
        assert updatedPlaylist.get().getTracks().size() == 1;
        assert !updatedPlaylist.get().getTracks().contains(track1);
        assert updatedPlaylist.get().getTracks().contains(track2);
    }

    public void testIncrementPlaylistFollowers_ShouldSuccessfullyIncrementFollowers() {
        // Arrange
        Playlist playlist = new Playlist("playlist1", "My Favorites", false, System.currentTimeMillis(), 5);
        playlistRepository.save(playlist);

        // Act
        playlist.setNumFollowers(playlist.getNumFollowers() + 1);
        playlistRepository.save(playlist);

        // Assert
        Optional<Playlist> updatedPlaylist = playlistRepository.findById("playlist1");
        assert updatedPlaylist.isPresent();
        assert updatedPlaylist.get().getNumFollowers() == 6;
    }
}
