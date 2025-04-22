package com.meloman.project.communications;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Represents the server's response to a client request.
 * Indicates success/failure, contains data (payload), or an error message.
 */

@Getter
@Setter

public class Response implements Serializable {
    private boolean success;
    private Object payload;
    private String errorMessage;

    public Response(boolean success, Object payload, String errorMessage) {
        this.success = success;
        this.payload = payload;
        this.errorMessage = errorMessage;
    }
}