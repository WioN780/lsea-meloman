package com.meloman.project.communications;

import com.meloman.project.service_model.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Client-side class handling user interactions and communication
 */

@Getter
@Setter

public class Client {
    private User currentUser;

    /**
     * Initializes connection to messaging server
     * @param serverAddress IP/hostname of server
     * @param port Server port number
     */
    public void connect(String serverAddress, int port) {
        //TODO: Implement connection establishment
    }

    /**
     * Sends message to another user through server
     * @param recipientId Target user ID
     * @param messageContent Text content to send
     */
    public void sendMessage(String recipientId, String messageContent) {
        //TODO: Implement message packaging and sending
    }

    /**
     * Updates user's active status on server
     * @param isActive New status value
     */
    public void updateActivityStatus(boolean isActive) {
        //TODO: Implement status update transmission
    }

    /**
     * Fetches current user list from server
     * @return List of available users with basic info
     */
    public List<User> requestUserList() {
        //TODO: Implement user list request/response handling
        return null;
    }
}