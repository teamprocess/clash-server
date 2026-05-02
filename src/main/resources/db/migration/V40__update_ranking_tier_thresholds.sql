UPDATE users
SET current_exp_tier = CASE
    WHEN total_exp >= 50000 THEN 'DIAMOND'
    WHEN total_exp >= 30000 THEN 'GOLD'
    WHEN total_exp >= 10000 THEN 'SILVER'
    WHEN total_exp >= 1000  THEN 'BRONZE'
    ELSE 'UNRANKED'
END;

WITH ranked_users AS (
    SELECT
        id,
        total_exp,
        ROW_NUMBER() OVER (ORDER BY total_exp DESC) AS rank
    FROM users
    WHERE user_status = 'ACTIVE'
)
UPDATE users u
SET current_rank_tier = CASE
    WHEN ru.rank = 1 AND ru.total_exp >= 150000 THEN 'AURA'
    WHEN ru.rank <= 3 AND ru.total_exp >= 100000 THEN 'MASTER'
    ELSE 'NONE'
END
FROM ranked_users ru
WHERE u.id = ru.id;

UPDATE users
SET current_rank_tier = 'NONE'
WHERE user_status <> 'ACTIVE';
