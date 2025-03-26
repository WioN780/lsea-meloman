package com.meloman.project.entry_points;

import com.meloman.project.data_model.*;

import java.util.*;

public class TestMain {
    public static void main(String[] args){

        Artist artist1 = new Artist("A1", "Artist One", "Real Name One", Arrays.asList("Alias1", "Alias2"), Arrays.asList("Group1"), new HashSet<>(Arrays.asList("www.artistone.com")));
        Artist artist2 = new Artist("A2", "Artist Two", "Real Name Two", Arrays.asList("Alias3", "Alias4"), Arrays.asList("Group2"), new HashSet<>(Arrays.asList("www.artisttwo.com")));

        // Create some dummy Labels
        Label label1 = new Label("L1", "Label One", "Parent Label One", Arrays.asList("SubLabel1", "SubLabel2"), new ArrayList<>(), new HashSet<>(Arrays.asList("www.labelone.com")), "contact@labelone.com");
        Label label2 = new Label("L2", "Label Two", "Parent Label Two", Arrays.asList("SubLabel3", "SubLabel4"), new ArrayList<>(), new HashSet<>(Arrays.asList("www.labeltwo.com")), "contact@labeltwo.com");

        // Create some dummy Tracks
        Track track1 = new Track("T1", "Track One", 2020, new HashSet<>(Arrays.asList("Rock", "Pop")), new HashSet<>(Arrays.asList("Indie")), artist1, label1, 3.5);
        Track track2 = new Track("T2", "Track Two", 2020, new HashSet<>(Arrays.asList("Pop", "R&B")), new HashSet<>(Arrays.asList("Soul")), artist1, label1, 4.0);
        Track track3 = new Track("T3", "Track Three", 2019, new HashSet<>(Arrays.asList("Jazz", "Blues")), new HashSet<>(Arrays.asList("Soul")), artist2, label2, 4.2);

        // Create some dummy Albums
        Album album1 = new Album("AL1", "Album One", 2020, new HashSet<>(Arrays.asList("Rock", "Pop")), new HashSet<>(Arrays.asList("Indie", "Alternative")), artist1, label1, Arrays.asList(track1, track2));
        Album album2 = new Album("AL2", "Album Two", 2019, new HashSet<>(Arrays.asList("Jazz", "Blues")), new HashSet<>(Arrays.asList("Soul", "Classic")), artist2, label2, Arrays.asList(track3));

        // Display information about albums
        album1.displayInfo();
        album2.displayInfo();

        // Test cloning
        Album clonedAlbum = album1.clone();
        System.out.println("\nCloned Album:");
        clonedAlbum.displayInfo();

        // Sorting Albums by Year (Comparable)
        List<Album> albumList = new ArrayList<>();
        albumList.add(album1);
        albumList.add(album2);

        albumList.sort(Comparator.naturalOrder()); // Sort by year using Comparable
        System.out.println("\nSorted Albums by Year:");
        for (Album album : albumList) {
            System.out.println(album.getTitle() + " (" + album.getYear() + ")");
        }

        // Sorting Albums by Title (Comparator)
        albumList.sort(new AlbumTitleComparator()); // Sort by title using Comparator
        System.out.println("\nSorted Albums by Title:");
        for (Album album : albumList) {
            System.out.println(album.getTitle());
        }

        // Test MediaOwner and Polymorphism
        MediaOwner mediaOwner = new Artist("A3", "Artist Three", Arrays.asList(album1, album2), "Real Name Three", Arrays.asList("Alias5"), Arrays.asList("Group3"), new HashSet<>());
        System.out.println("\nMedia Owner: " + mediaOwner);


        System.out.print("Hello Testing");
    }


}
