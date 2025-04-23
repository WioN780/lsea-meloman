package com.meloman.project.communications;

import lombok.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;   // ⇠ good practice

    /** Mandatory client signature */
    private final UUID userId;

    /** The kind of action that is requested */
    private final RequestType type;

    /** Primary data for the request (can be null) */
    private final Serializable payload;

    /** Arbitrary extra parameters */
    @Builder.Default
    private final Map<String, Serializable> params = new HashMap<>();

    /** Alternate constructor when you have no extra params. */
    public Request(UUID userId, RequestType type) {
        this(userId, type, null, new HashMap<>());
    }

    /** Alternate constructor when you have no extra params. */
    public Request(UUID userId, RequestType type, Serializable payload) {
        this(userId, type, payload, new HashMap<>());
    }

    /** Add / replace a parameter fluently */
    public Request withParam(String key, Serializable value) {
        params.put(key, value);
        return this;
    }

    /** Typed accessor */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getParam(String key, Class<T> klass) {
        return (T) params.get(key);
    }
}
