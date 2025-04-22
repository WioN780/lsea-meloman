package com.meloman.project.communications;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a client's request to the server.
 * Contains the request type and parameters.
 */

@Getter
@Setter

public class Request implements Serializable {
    private RequestType type;
    private String payload;
    private Map<String, Object> params;

    public Request() {
        this.params = new HashMap<>();
    }

    public Request(RequestType type) {
        this.type = type;
        this.params = new HashMap<>();
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    public Object getParam(String key) {
        return params.get(key);
    }

    public static Request fromString(String rawData) {
        Request request = new Request();
        try {
            String[] parts = rawData.split(":", 2);
            request.setType(RequestType.valueOf(parts[0].toUpperCase()));
            if (parts.length > 1) {
                request.setPayload(parts[1]);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request type: " + rawData);
        }
        return request;
    }
}