ALTER TABLE users
    DROP COLUMN IF EXISTS total_token;

ALTER TABLE products
    DROP COLUMN IF EXISTS type;

ALTER TABLE purchases
    DROP COLUMN IF EXISTS goods_type;

ALTER TABLE user_goods_history
    DROP COLUMN IF EXISTS goods_type;
