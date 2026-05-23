package io.irn.geoloc.events.domain.model;

import io.irn.geoloc.shared.kernel.ValueObject;

/**
 * Value object representing an event category.
 */
public class Category implements ValueObject {
    private final String id;
    private final String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
