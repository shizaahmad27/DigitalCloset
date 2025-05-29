-- Remove the old items column from outfits
ALTER TABLE outfits DROP COLUMN IF EXISTS items;

-- Create the join table for the many-to-many relationship
CREATE TABLE outfit_clothing_items (
    outfit_id BIGINT NOT NULL,
    clothing_item_id BIGINT NOT NULL,
    PRIMARY KEY (outfit_id, clothing_item_id),
    CONSTRAINT fk_outfit FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    CONSTRAINT fk_clothing_item FOREIGN KEY (clothing_item_id) REFERENCES clothing_items(id) ON DELETE CASCADE
); 