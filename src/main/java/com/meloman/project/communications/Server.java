package com.meloman.project.communications;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

/*
 * Server-side class managing user connections and message routing
 */

public class Server {
    private List<User> connectedUsers;
    private int port;

    /**
     * Starts listening for incoming client connections
     * @param port Network port to listen on
     */
    public void start(int port) {
        //TODO: Implement TCP/UDP server socket initialization
    }

    /**
     * Handles incoming client requests and routes them to appropriate handlers
     * @param request Received request object
     */
    private void handleRequest(Object request) {
        //TODO: Implement request type distinction
        //Should call specific handlers based on request type
    }

    /**
     * Maintains list of currently connected users
     * @param user User to add/update in the list
     */
    public void updateUserStatus(User user) {
        //TODO: Add/update user in connectedUsers list
    }

    /**
     * Broadcasts message status updates to relevant clients
     * @param messageId ID of message to update
     * @param recipientId Target user for status update
     */
    public void broadcastMessageStatus(String messageId, String recipientId) {
        //TODO: Implement status propagation logic
    }
}