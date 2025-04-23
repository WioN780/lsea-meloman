package com.meloman.project.communications;

import lombok.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    /** true = OK, false = something went wrong */
    private final boolean success;

    /** Main result (album list, playlists, etc.) */
    private final Serializable data;

    /** Extra explanatory text, warning, can be `null` */
    private final String message;

    /** Arbitrary key/value pairs that callers might find useful */
    @Builder.Default
    private final Map<String, Serializable> meta = new HashMap<>();

    public Response(boolean success, Serializable data, String message) {
        this(success, data, message, new HashMap<>());
    }
}
