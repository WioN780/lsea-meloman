package com.meloman.project.communications;

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.service_model.User;

import java.util.ArrayList;
import java.util.List;

public class RequestHandler {

    private final Server server;

    public RequestHandler(Server server) {
        this.server = server;
    }

    public Response handle(Request req) {
        switch (req.getType()) {

            case GET_10_RANDOM_ALBUMS -> {
                ArrayList<Album> albums = server.getRandomItems(
                        server.getAvailableAlbums(), 10);
                return new Response(true, albums, "10 random albums");
            }

            case GET_10_RANDOM_PLAYLISTS -> {
                ArrayList<Playlist> playlists = server.getRandomItems(
                        server.getAvailablePlaylists(), 10);
                return new Response(true, playlists, "10 random playlists");
            }

            case ADD_NEW_USER -> {
                // User u = (User) req.getPayload();
                // TODO validate & persist…
                return new Response(true, null, "User added");
            }

            // …add the rest of the cases here…

            default -> {
                return new Response(false, null,
                        "Unsupported request type: " + req.getType());
            }
        }
    }
}
