-- TOKEN currency is discontinued. Normalize historical data to COOKIE.

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
