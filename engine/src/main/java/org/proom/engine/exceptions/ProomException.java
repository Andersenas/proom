package org.proom.engine.exceptions;

import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author vasyalike
 */
public class ProomException extends RuntimeException {
    private final Map<String, Object> data;

    public ProomException(Map<String, Object> data) {
        super(data.toString());
        this.data = data;
    }

    public final Map<String, Object> getData() {
        return unmodifiableMap(data);
    }
}
