package com.meloman.project.entry_points;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.utils.AnalysisRunner;
import com.meloman.project.utils.DiscoGSLoader;
import com.meloman.project.utils.SpotifyPlaylistLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainLab34 {
    public static void main(String[] args) throws IOException {
        List<Playlist> playlists = new ArrayList<>();

        int num_of_slices = 300;

        SpotifyPlaylistLoader loaderPlaylists = new SpotifyPlaylistLoader();

        for (int j = 0; j < num_of_slices; j++) {
            try {
                String file = "spotify/mpd.slice." + 1000 * j + "-" + ((1000 * (j + 1)) - 1) + ".json";

                InputStream inputStreamPlaylists = SpotifyPlaylistLoader.class.getClassLoader()
                        .getResourceAsStream(file);

                if (inputStreamPlaylists == null) {
                    throw new IOException("File not found." + file);
                }

                playlists.addAll(loaderPlaylists.loadPlaylists(inputStreamPlaylists));
                System.out.println(playlists.size() + " playlists loaded");

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        DiscoGSLoader loaderAlbums = new DiscoGSLoader();

        InputStream inputStreamAlbums = SpotifyPlaylistLoader.class.getClassLoader()
                .getResourceAsStream("DiscoGSdata.cleaned.csv");

        if (inputStreamAlbums == null) {
            throw new IOException("File not found: DiscoGSdata.cleaned.csv");
        }

        List<Album> albums = loaderAlbums.loadAlbums(inputStreamAlbums);
        System.out.println(albums.size() + " albums loaded");
        System.out.println(playlists.size() + " playlists loaded");

        AnalysisRunner ar = new AnalysisRunner(albums, playlists);

        ar.singleThreadAnalysis();
        ar.parallelThreadAnalysis();
    }
}
