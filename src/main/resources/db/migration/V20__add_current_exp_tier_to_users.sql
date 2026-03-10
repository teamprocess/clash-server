ALTER TABLE users ADD COLUMN current_exp_tier VARCHAR(255) NOT NULL DEFAULT 'UNRANKED';

UPDATE users
SET current_exp_tier = CASE
    WHEN total_exp >= 50000 THEN 'DIAMOND'
    WHEN total_exp >= 30000 THEN 'GOLD'
    WHEN total_exp >= 10000 THEN 'SILVER'
    WHEN total_exp >= 1000  THEN 'BRONZE'
    ELSE 'UNRANKED'
END;