UPDATE users
SET total_cookie = total_cookie + total_token,
    total_token = 0
WHERE total_token <> 0;

UPDATE products
SET type = 'COOKIE'
WHERE type = 'TOKEN';

UPDATE purchases
SET goods_type = 'COOKIE'
WHERE goods_type = 'TOKEN';

UPDATE user_goods_history
SET goods_type = 'COOKIE'
WHERE goods_type = 'TOKEN';

ALTER TABLE users
    DROP COLUMN IF EXISTS total_token;

ALTER TABLE products
    DROP COLUMN IF EXISTS type;

ALTER TABLE purchases
    DROP COLUMN IF EXISTS goods_type;

ALTER TABLE user_goods_history
    DROP COLUMN IF EXISTS goods_type;
