package com.meloman.project.communications;

/*
 * Handles different types of server requests.
 */

import com.meloman.project.data_model.Album;
import com.meloman.project.data_model.Playlist;
import com.meloman.project.service_model.User;

import java.util.List;

public class RequestHandler {
    private Server server;

    public RequestHandler(Server server) {
        this.server = server;
    }

    /**
     * Processes incoming message delivery requests
     * @param message Raw message object from client
     */
    public void handleMessageDelivery(Object message) {
        // TODO: Implement message validation and routing
    }

    /**
     * Handles user status update requests
     * @param statusUpdate User's new status information
     */
    public void handleStatusUpdate(User statusUpdate) {
        // TODO: Implement status propagation to other clients
    }

    /**
     * Processes user list requests
     * @return Serialized user list data
     */
    public Object handleUserListRequest() {
        // TODO: Implement user list packaging
        return null;
    }

    /**
     * Handles requests for random albums
     * @return Response object with random albums
     */
    public Response handleRandomAlbumsRequest() {
        List<Album> randomAlbums = server.getRandomItems(server.getAvailableAlbums(), 10);

        if (randomAlbums.isEmpty()) {
            return new Response(false, null, "No albums available");
        }

        return new Response(true, randomAlbums, null);
    }

    /**
     * Handles requests for random playlists
     * @return Response object with random playlists
     */
    public Response handleRandomPlaylistsRequest() {
        List<Playlist> randomPlaylists = server.getRandomItems(server.getAvailablePlaylists(), 10);

        if (randomPlaylists.isEmpty()) {
            return new Response(false, null, "No playlists available");
        }

        return new Response(true, randomPlaylists, null);
    }
}