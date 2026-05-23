package io.irn.geoloc.events.geolocation.model;

import io.irn.geoloc.shared.kernel.ValueObject;

/**
 * Value object representing geographic coordinates.
 */
public class Coordinates implements ValueObject {
    private final Double latitude;
    private final Double longitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getId() {
        // Coordinates are identified by their lat/lon pair.
        return latitude + "," + longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
