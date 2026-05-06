ALTER TABLE products
    ADD COLUMN is_able_purchase BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE products
SET is_able_purchase = TRUE;