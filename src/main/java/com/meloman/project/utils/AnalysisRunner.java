package com.meloman.project.utils;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;

import java.util.List;

public class AnalysisRunner {

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
     * Performs single-threaded analysis on the provided data.
     * Also logs time spent on completion
     */
    public void singleThreadAnalysis() {
        System.out.println("Starting single-threaded analysis...");
        long start = System.currentTimeMillis();

        // logic to be implemented

        long end = System.currentTimeMillis();
        System.out.println("Single-threaded analysis completed in " + (end - start) + " ms.");
    }

    /**
     * Performs parallel-threaded analysis on the provided data.
     * Also logs time spent on completion
     */
    public void parallelThreadAnalysis() {
        System.out.println("Starting parallel-threaded analysis...");
        long start = System.currentTimeMillis();

        // logic to be implemented

        long end = System.currentTimeMillis();
        System.out.println("Parallel analysis completed in " + (end - start) + " ms.");
    }
}
