-- V2__create_event_locations_table.sql
-- Create table for event locations with PostGIS geometry

CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE event_locations (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL,
    coordinates GEOGRAPHY(POINT, 4326) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events(id)
);

CREATE INDEX idx_event_locations_coordinates ON event_locations USING GIST (coordinates);
