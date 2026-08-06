ALTER TABLE users
    RENAME COLUMN profile_image_url TO profile_image_public_id;

ALTER TABLE trip_images
    DROP COLUMN url;