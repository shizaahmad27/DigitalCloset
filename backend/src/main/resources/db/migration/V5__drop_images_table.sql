-- Drop foreign key constraints first
ALTER TABLE clothing_items DROP CONSTRAINT IF EXISTS fk_clothing_item_image;

-- Drop the image_id column from clothing_items
ALTER TABLE clothing_items DROP COLUMN IF EXISTS image_id;

-- Drop the images table
DROP TABLE IF EXISTS images; 