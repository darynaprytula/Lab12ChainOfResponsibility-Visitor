package org.example.task2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Task<T> {
    private String id;
    private Map<String, String> headers;

    public abstract void apply(T arg);

    public void freeze() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    protected void setHeader(String header, String headerValue) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(header, headerValue);
    }

    public String getHeader(String header) {
        if (headers == null) {
            return null;
        }
        return headers.get(header);
    }

    public Map<String, String> getAllHeaders() {
        return headers == null ? Map.of() : Map.copyOf(headers);
    }
}
