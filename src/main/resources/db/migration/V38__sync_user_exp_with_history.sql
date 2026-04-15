-- 베타 시즌 종료 후 일괄 exp 회수로 인해 users.total_exp가
-- user_exp_history 누적 합계와 불일치하는 상태를 동기화한다.
-- earn_exp 합계가 음수가 되는 경우(회수 이후 추가 획득 없는 유저)는 0으로 처리한다.

UPDATE users u
SET total_exp = GREATEST(0, COALESCE(
    (SELECT SUM(ueh.earn_exp)
     FROM user_exp_history ueh
     WHERE ueh.fk_user_id = u.id),
    0
));

-- 보정된 total_exp 기준으로 current_exp_tier 재계산
UPDATE users
SET current_exp_tier = CASE
    WHEN total_exp >= 100000 THEN 'DIAMOND'
    WHEN total_exp >= 30000 THEN 'GOLD'
    WHEN total_exp >= 10000 THEN 'SILVER'
    WHEN total_exp >= 1000  THEN 'BRONZE'
    ELSE 'UNRANKED'
END;