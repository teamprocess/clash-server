ALTER TABLE products ADD COLUMN audio VARCHAR(255);

ALTER TABLE user_equipped_items
    DROP CONSTRAINT IF EXISTS user_equipped_items_category_check;
ALTER TABLE user_equipped_items
    ADD CONSTRAINT user_equipped_items_category_check
    CHECK (category IN ('INSIGNIA', 'NAMEPLATE', 'BANNER', 'BGM'));
