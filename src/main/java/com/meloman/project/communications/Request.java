package com.meloman.project.communications;

import lombok.Getter;
import lombok.Setter;

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
    private Map<String, Object> params;

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

}