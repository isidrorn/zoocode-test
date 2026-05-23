package io.irn.geoloc.events.domain.model;

import io.irn.geoloc.shared.kernel.ValueObject;

/**
 * Value object representing the organizer of an event.
 */
public class Organizer implements ValueObject {
    private final String id;
    private final String name;
    private final String contactEmail;

    public Organizer(String id, String name, String contactEmail) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }
}
