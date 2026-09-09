ALTER TABLE trips
    RENAME COLUMN distance TO distance_meters;

ALTER TABLE trips
    RENAME COLUMN estimated_duration TO estimated_duration_seconds;

ALTER TABLE trips
ALTER COLUMN estimated_duration_seconds TYPE BIGINT
    USING estimated_duration_seconds::BIGINT;

ALTER TABLE trips
    ALTER COLUMN estimated_duration_seconds SET NOT NULL;