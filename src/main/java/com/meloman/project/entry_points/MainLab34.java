package com.meloman.project.entry_points;

import com.meloman.project.transaction_model.AlbumT;
import com.meloman.project.transaction_model.PlaylistT;
import com.meloman.project.utils.AnalysisRunner;
import com.meloman.project.utils.DiscoGSLoader;
import com.meloman.project.utils.SpotifyPlaylistLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainLab34 {
    public static void main(String[] args) throws IOException {
        List<PlaylistT> playlistTS = new ArrayList<>();

        int num_of_slices = 100;

        SpotifyPlaylistLoader loaderPlaylists = new SpotifyPlaylistLoader();

        for (int j = 0; j < num_of_slices; j++) {
            try {
                String file = "spotify/mpd.slice." + 1000 * j + "-" + ((1000 * (j + 1)) - 1) + ".json";

                InputStream inputStreamPlaylists = SpotifyPlaylistLoader.class.getClassLoader()
                        .getResourceAsStream(file);

                if (inputStreamPlaylists == null) {
                    throw new IOException("File not found." + file);
                }

                playlistTS.addAll(loaderPlaylists.loadPlaylists(inputStreamPlaylists));
                System.out.println(playlistTS.size() + " playlists loaded");

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

        List<AlbumT> albumTS = loaderAlbums.loadAlbums(inputStreamAlbums);
        System.out.println(albumTS.size() + " albums loaded");
        System.out.println(playlistTS.size() + " playlists loaded");

        AnalysisRunner ar = new AnalysisRunner(albumTS, playlistTS);

        ar.singleThreadAnalysis();
        ar.parallelThreadAnalysis(2);
        ar.parallelThreadAnalysis(3);
        ar.parallelThreadAnalysis(4);
    }
}
