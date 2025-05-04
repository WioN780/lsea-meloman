package com.meloman.project.communications;

import com.meloman.project.transaction_model.Album;
import com.meloman.project.transaction_model.MediaItem;
import com.meloman.project.transaction_model.Playlist;
import com.meloman.project.service_model.User;

import java.util.ArrayList;
import java.util.UUID;

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
                if (req.getPayload() instanceof User) {
                    // Direct modification of the list
                    server.getConnectedUsers().add((User) req.getPayload());
                    return new Response(true, null, "User added");
                }
                return new Response(false, null, "User not added, wrong data.");
            }

            case VIEW_LIKED_ITEMS -> {

                for (int i = 0; i < server.getConnectedUsers().size(); i++){
                    if (server.getConnectedUsers().get(i).getUserId().equals(req.getUserId())){
                        return new Response(true, server.getConnectedUsers().get(i).getLikedItems(), "Liked items");
                    }
                }
            }

            case DELETE_USER -> {
                if (req.getPayload() instanceof User) {
                    User user = (User) req.getPayload();
                    UUID userId = (UUID) user.getUserId();

                    // Remove directly from the list
                    server.getConnectedUsers().removeIf(u -> u.getUserId().equals(userId));
                }
            }

            case ADD_USER_LIKED_ITEM -> {
                if (req.getPayload() instanceof MediaItem) {
                    MediaItem item = (MediaItem) req.getPayload();
                    UUID userId = (UUID) req.getUserId();

                    for (int i = 0; i < server.getConnectedUsers().size(); i++){
                        if (server.getConnectedUsers().get(i).getUserId().equals(req.getUserId())){
                            server.getConnectedUsers().get(i).likeItem(item);
                            return new Response(true, null, "Item liked");
                        }
                    }

                }
            }


            default -> {
                return new Response(false, null,
                        "Unsupported request type: " + req.getType());
            }
        }
        return new Response(false, null, "Something went wrong");
    }
}
