-- Drop the existing check constraint
ALTER TABLE clothing_item_event_types DROP CONSTRAINT IF EXISTS clothing_item_event_types_chk_1;

-- Add new check constraint for EventType enum values
ALTER TABLE clothing_item_event_types 
ADD CONSTRAINT clothing_item_event_types_chk_1 
CHECK (event_type IN ('DAY', 'NIGHT', 'DATE', 'CASUAL', 'BEACH', 'SCHOOL', 'FORMAL', 'SPORTS', 'WORK')); 