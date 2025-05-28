-- Create images table
CREATE TABLE images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Add image_id column to clothing_items
ALTER TABLE clothing_items ADD COLUMN image_id BIGINT;

-- Add foreign key constraint
ALTER TABLE clothing_items 
ADD CONSTRAINT fk_clothing_item_image 
FOREIGN KEY (image_id) REFERENCES images(id);

-- Drop the old image_url column since we're now using the Image entity
ALTER TABLE clothing_items DROP COLUMN image_url; 