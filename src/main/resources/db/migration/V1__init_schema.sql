CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    clerk_Id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    profile_image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE trips (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    estimated_duration VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    creator_id BIGINT NOT NULL,

    CONSTRAINT fk_trips_creator
                  FOREIGN KEY (creator_id)
                  REFERENCES users(id)
                  ON DELETE RESTRICT
);

CREATE TABLE trip_images (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    public_id VARCHAR(255) NOT NULL,
    trip_id BIGINT NOT NULL,

    CONSTRAINT fk_trip_images_trip
                         FOREIGN KEY (trip_id)
                         REFERENCES trips(id)
                         ON DELETE CASCADE
);

CREATE INDEX idx_trips_creator_id ON trips(creator_id);
CREATE INDEX idx_trip_images_trip_id ON trip_images(trip_id);
