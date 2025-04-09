package com.meloman.project.utils;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.data_model.Track;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class AnalysisRunner {

    private final static Integer TIMES = 10;

    private List<Album> albums;
    private List<Playlist> playlists;

    /**
     * Constructs an AnalysisRunner with the provided lists of albums and playlists.
     *
     * @param albums the list of Album objects to analyze
     * @param playlists the list of Playlist objects to analyze
     */
    public AnalysisRunner(List<Album> albums, List<Playlist> playlists) {
        this.albums = albums;
        this.playlists = playlists;
    }

    /**
     * Both methods below in order to complete the task
     * are made to compute the same query: "Count sum of number of appearances of every song for an album"
     */

    /**
     * Performs single-threaded analysis on the provided data a couple of times.
     * Also logs time spent on completion
     */
    public void singleThreadAnalysis() {
        System.out.println("Starting single-threaded analysis " + TIMES + " times");

        for (int i = 0; i < TIMES; i++) {
            long start = System.currentTimeMillis();

            //map every track to its album
            Map<String, Album> trackIdToAlbum = albums.stream().sequential()
                    .flatMap(album -> album.getTracks().stream()
                            .map(track -> new AbstractMap.SimpleEntry<>(track.getTitle(), album)))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> existing // keep the first
                    ));


            Map<Album, Integer> albumToCount = playlists.stream().sequential()
                    // Flatten the playlists into a stream of tracks
                    .flatMap(playlist -> playlist.getTracks().stream())
                    // Filter out tracks that don't have an album
                    .filter(track -> trackIdToAlbum.get(track.getTitle()) != null)
                    // Group tracks by their album
                    .collect(Collectors.groupingBy(
                            track -> trackIdToAlbum.get(track.getTitle()),
                            // For each track occurrence add 1
                            Collectors.summingInt(track -> 1)
                    ));

            //sorting by integer
            List<Map.Entry<Album, Integer>> sortedAlbumCounts = albumToCount.entrySet()
                    .stream().sequential()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());


            long end = System.currentTimeMillis();
            System.out.println("Single-threaded analysis №" + (i + 1) + " completed in " + (end - start) + " ms.");

            Map.Entry<Album, Integer> topEntry = sortedAlbumCounts.get(0);
            Album topAlbum = topEntry.getKey();
            int topCount = topEntry.getValue();

            System.out.println("Most occurring album:");
            System.out.println("Album: \"" + topAlbum.getTitle() + "\" by " + topAlbum.getArtist().getName());
            System.out.println("Occurrences: " + topCount);
        }
    }

    /**
     * Performs parallel-threaded analysis on the provided data a couple of times.
     * Also logs time spent on completion
     * @param numOfThreads Specifying number of threads
     */
    public void parallelThreadAnalysis(int numOfThreads) {
        System.out.println("Starting parallel-threaded analysis " + TIMES + " times. " + numOfThreads + " Threads.");
        ForkJoinPool customThreadPool = new ForkJoinPool(numOfThreads);

        try {
            customThreadPool.submit(() -> {
                for (int i = 0; i < TIMES; i++) {
                    long start = System.currentTimeMillis();

                    //map every track to its album
                    Map<String, Album> trackIdToAlbum = albums.stream().parallel()
                            .flatMap(album -> album.getTracks().stream()
                                    .map(track -> new AbstractMap.SimpleEntry<>(track.getTitle(), album)))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (existing, replacement) -> existing // keep the first
                            ));


                    Map<Album, Integer> albumToCount = playlists.stream().parallel()
                            // Flatten the playlists into a stream of tracks
                            .flatMap(playlist -> playlist.getTracks().stream())
                            // Filter out tracks that don't have an album
                            .filter(track -> trackIdToAlbum.get(track.getTitle()) != null)
                            // Group tracks by their album
                            .collect(Collectors.groupingBy(
                                    track -> trackIdToAlbum.get(track.getTitle()),
                                    // For each track occurrence add 1
                                    Collectors.summingInt(track -> 1)
                            ));

                    //sorting by integer
                    List<Map.Entry<Album, Integer>> sortedAlbumCounts = albumToCount.entrySet()
                            .stream().parallel()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toList());

                    long end = System.currentTimeMillis();
                    System.out.println("Parallel analysis №" + (i + 1) + " completed in " + (end - start) + " ms.");

                    Map.Entry<Album, Integer> topEntry = sortedAlbumCounts.get(0);
                    Album topAlbum = topEntry.getKey();
                    int topCount = topEntry.getValue();

                    System.out.println("Most occurring album:");
                    System.out.println("Album: \"" + topAlbum.getTitle() + "\" by " + topAlbum.getArtist().getName());
                    System.out.println("Occurrences: " + topCount);
                }
            }).get(); // Wait for task to complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
                customThreadPool.shutdown();
        }
    }
}
