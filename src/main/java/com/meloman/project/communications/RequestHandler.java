package com.meloman.project.communications;

/*
 * Handles different types of server requests.
 */

import com.meloman.project.service_model.User;

public class RequestHandler {
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
}
