package com.meloman.project.communications;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents a user in the messaging system.
 * Contains basic user information for client-server communication.
 */

@Getter
@Setter

public class User {
    private String userId;
    private boolean isActive;
    private Date lastSeen;

    /**
     * @param userId Unique identifier for the user
     * @param isActive Whether user is currently online
     * @param lastSeen Last activity timestamp
     */
    public User(String userId, boolean isActive, Date lastSeen) {
        this.userId = userId;
        this.isActive = isActive;
        this.lastSeen = lastSeen;
    }
}