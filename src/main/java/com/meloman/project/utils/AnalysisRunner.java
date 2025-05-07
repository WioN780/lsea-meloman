package com.meloman.project.utils;

import com.meloman.project.transaction_model.AlbumT;
import com.meloman.project.transaction_model.PlaylistT;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class AnalysisRunner {

    private final static Integer TIMES = 10;

    private List<AlbumT> albumTS;
    private List<PlaylistT> playlistTS;

    /**
     * Constructs an AnalysisRunner with the provided lists of albums and playlists.
     *
     * @param albumTS the list of Album objects to analyze
     * @param playlistTS the list of Playlist objects to analyze
     */
    public AnalysisRunner(List<AlbumT> albumTS, List<PlaylistT> playlistTS) {
        this.albumTS = albumTS;
        this.playlistTS = playlistTS;
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
            Map<String, AlbumT> trackIdToAlbum = albumTS.stream().sequential()
                    .flatMap(album -> album.getTrackTS().stream()
                            .map(track -> new AbstractMap.SimpleEntry<>(track.getTitle(), album)))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (existing, replacement) -> existing // keep the first
                    ));


            Map<AlbumT, Integer> albumToCount = playlistTS.stream().sequential()
                    // Flatten the playlists into a stream of tracks
                    .flatMap(playlist -> playlist.getTrackTS().stream())
                    // Filter out tracks that don't have an album
                    .filter(track -> trackIdToAlbum.get(track.getTitle()) != null)
                    // Group tracks by their album
                    .collect(Collectors.groupingBy(
                            track -> trackIdToAlbum.get(track.getTitle()),
                            // For each track occurrence add 1
                            Collectors.summingInt(track -> 1)
                    ));

            //sorting by integer
            List<Map.Entry<AlbumT, Integer>> sortedAlbumCounts = albumToCount.entrySet()
                    .stream().sequential()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());


            long end = System.currentTimeMillis();
            System.out.println("Single-threaded analysis №" + (i + 1) + " completed in " + (end - start) + " ms.");

            Map.Entry<AlbumT, Integer> topEntry = sortedAlbumCounts.get(0);
            AlbumT topAlbumT = topEntry.getKey();
            int topCount = topEntry.getValue();

            System.out.println("Most occurring album:");
            System.out.println("Album: \"" + topAlbumT.getTitle() + "\" by " + topAlbumT.getArtistT().getName());
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
                    Map<String, AlbumT> trackIdToAlbum = albumTS.stream().parallel()
                            .flatMap(album -> album.getTrackTS().stream()
                                    .map(track -> new AbstractMap.SimpleEntry<>(track.getTitle(), album)))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (existing, replacement) -> existing // keep the first
                            ));


                    Map<AlbumT, Integer> albumToCount = playlistTS.stream().parallel()
                            // Flatten the playlists into a stream of tracks
                            .flatMap(playlist -> playlist.getTrackTS().stream())
                            // Filter out tracks that don't have an album
                            .filter(track -> trackIdToAlbum.get(track.getTitle()) != null)
                            // Group tracks by their album
                            .collect(Collectors.groupingBy(
                                    track -> trackIdToAlbum.get(track.getTitle()),
                                    // For each track occurrence add 1
                                    Collectors.summingInt(track -> 1)
                            ));

                    //sorting by integer
                    List<Map.Entry<AlbumT, Integer>> sortedAlbumCounts = albumToCount.entrySet()
                            .stream().parallel()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toList());

                    long end = System.currentTimeMillis();
                    System.out.println("Parallel analysis №" + (i + 1) + " completed in " + (end - start) + " ms.");

                    Map.Entry<AlbumT, Integer> topEntry = sortedAlbumCounts.get(0);
                    AlbumT topAlbumT = topEntry.getKey();
                    int topCount = topEntry.getValue();

                    System.out.println("Most occurring album:");
                    System.out.println("Album: \"" + topAlbumT.getTitle() + "\" by " + topAlbumT.getArtistT().getName());
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
