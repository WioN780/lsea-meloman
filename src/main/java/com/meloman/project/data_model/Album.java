package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

@Getter
@Setter
public class Album extends MediaItem {

    private List<Track> tracks;

    public Album(String id, String title, int year, List<String> genres, List<String> styles, Artist artist, Label label) {
        super(id, title, year, genres, styles, artist, label);
        this.tracks = new ArrayList<>();
    }

    @Override
    public void displayInfo() {
        System.out.println("Album: " + getTitle() + " by " + artist.getName());
        System.out.println("Year: " + getYear());
        System.out.println("Genres: " + getGenres());
        System.out.println("Tracks:");
        int i = 1;
        for (Track track : tracks) {
            System.out.println(" - " + i + ": " + track.getTitle());
            i++;
        }
    }


    @Override
    public Album clone() {
        Album cloned = (Album) super.clone();
        List<Track> clonedTracks = new ArrayList<>();
        for (Track track : this.tracks) {
            clonedTracks.add(track.clone());
        }
        cloned.setTracks(clonedTracks);
        return cloned;
    }

    @Override
    public String toString() {
        String trackList = IntStream.range(0, tracks.size())
                .mapToObj(i -> (i + 1) + ". " + tracks.get(i).toString())
                .collect(Collectors.joining(";\n"));

        return "Album [id=" + getId() + ", title=" + getTitle() + ", artist=" + artist.getName() + ", label=" + label.getName() +
                ", year=" + getYear() + ", genres=" + getGenres() + ", styles=" + getStyles() +
                ", tracks:\n" + trackList + "]";
    }
}