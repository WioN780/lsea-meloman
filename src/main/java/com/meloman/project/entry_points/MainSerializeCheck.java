package com.meloman.project.entry_points;

import com.meloman.project.transaction_model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class MainSerializeCheck {
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        ArtistT artistT1 = new ArtistT("A1", "Artist One", "Real Name One", Arrays.asList("Alias1", "Alias2"), Arrays.asList("Group1"), new HashSet<>(Arrays.asList("www.artistone.com")));
        ArtistT artistT2 = new ArtistT("A2", "Artist Two", "Real Name Two", Arrays.asList("Alias3", "Alias4"), Arrays.asList("Group2"), new HashSet<>(Arrays.asList("www.artisttwo.com")));

        // Create some dummy Labels
        LabelT labelT1 = new LabelT("L1", "Label One", "Parent Label One", new HashSet<>(Arrays.asList("SubLabel1", "SubLabel2")), new ArrayList<>(), new HashSet<>(Arrays.asList("www.labelone.com")), "contact@labelone.com");
        LabelT labelT2 = new LabelT("L2", "Label Two", "Parent Label Two", new HashSet<>(Arrays.asList("SubLabel3", "SubLabel4")), new ArrayList<>(), new HashSet<>(Arrays.asList("www.labeltwo.com")), "contact@labeltwo.com");

        // Create some dummy Tracks
        TrackT trackT1 = new TrackT("T1", "Track One", 2020, new HashSet<>(Arrays.asList("Rock", "Pop")), new HashSet<>(Arrays.asList("Indie")), artistT1, labelT1, 3.5);
        TrackT trackT2 = new TrackT("T2", "Track Two", 2020, new HashSet<>(Arrays.asList("Pop", "R&B")), new HashSet<>(Arrays.asList("Soul")), artistT1, labelT1, 4.0);
        TrackT trackT3 = new TrackT("T3", "Track Three", 2019, new HashSet<>(Arrays.asList("Jazz", "Blues")), new HashSet<>(Arrays.asList("Soul")), artistT2, labelT2, 4.2);

        // Create some dummy Albums
        AlbumT albumT1 = new AlbumT("AL1", "Album One", 2020, new HashSet<>(Arrays.asList("Rock", "Pop")), new HashSet<>(Arrays.asList("Indie", "Alternative")), artistT1, labelT1, Arrays.asList(trackT1, trackT2));
        AlbumT albumT2 = new AlbumT("AL2", "Album Two", 2019, new HashSet<>(Arrays.asList("Jazz", "Blues")), new HashSet<>(Arrays.asList("Soul", "Classic")), artistT2, labelT2, Arrays.asList(trackT3));

        // Display information about albums
        albumT1.displayInfo();

        // Serialization
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("mediaTest.ser"));
        out.writeObject(albumT1);
        out.close();

        // Display information about albums
        albumT1.displayInfo();

        // Deserialization
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("mediaTest.ser"));
        MediaItem loadedItem = (MediaItem) in.readObject();
        in.close();

        // Delete the file
        File file = new File("mediaTest.ser");
        if (file.delete()) {
            System.out.println("Serialized file deleted successfully.");
        } else {
            System.out.println("Failed to delete the file.");
        }


    }
}
