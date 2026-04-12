-- 레이스 컨디션으로 생성된 중복 PENDING 레코드 정리 후 unique partial index 추가
--
-- 제거 대상:
--   1) 같은 쌍 사이에 PENDING-PENDING 레코드가 2개인 경우 → ID가 큰 것(나중에 생긴 것) 제거
--   2) 같은 쌍 사이에 이미 ACCEPTED가 존재하는데 PENDING도 남아있는 경우 → PENDING 제거
--
-- user_notices.rival_id 는 ON DELETE SET NULL 이므로 rivals 삭제 전에 먼저 직접 삭제해야
-- orphan notice가 남지 않음

-- Step 1: 제거할 라이벌과 연결된 알림 먼저 삭제
DELETE FROM user_notices
WHERE rival_id IN (
    SELECT r2.id
    FROM rivals r1
    JOIN rivals r2
      ON LEAST(r1.fk_first_user_id, r1.fk_second_user_id)    = LEAST(r2.fk_first_user_id, r2.fk_second_user_id)
     AND GREATEST(r1.fk_first_user_id, r1.fk_second_user_id) = GREATEST(r2.fk_first_user_id, r2.fk_second_user_id)
     AND r1.id < r2.id
     AND r1.rival_linking_status = 'PENDING'
     AND r2.rival_linking_status = 'PENDING'

    UNION

    SELECT pending.id
    FROM rivals pending
    JOIN rivals accepted
      ON LEAST(pending.fk_first_user_id, pending.fk_second_user_id)    = LEAST(accepted.fk_first_user_id, accepted.fk_second_user_id)
     AND GREATEST(pending.fk_first_user_id, pending.fk_second_user_id) = GREATEST(accepted.fk_first_user_id, accepted.fk_second_user_id)
     AND pending.rival_linking_status  = 'PENDING'
     AND accepted.rival_linking_status = 'ACCEPTED'
);

-- Step 2: 중복/고아 라이벌 레코드 삭제
DELETE FROM rivals
WHERE id IN (
    SELECT r2.id
    FROM rivals r1
    JOIN rivals r2
      ON LEAST(r1.fk_first_user_id, r1.fk_second_user_id)    = LEAST(r2.fk_first_user_id, r2.fk_second_user_id)
     AND GREATEST(r1.fk_first_user_id, r1.fk_second_user_id) = GREATEST(r2.fk_first_user_id, r2.fk_second_user_id)
     AND r1.id < r2.id
     AND r1.rival_linking_status = 'PENDING'
     AND r2.rival_linking_status = 'PENDING'

    UNION

    SELECT pending.id
    FROM rivals pending
    JOIN rivals accepted
      ON LEAST(pending.fk_first_user_id, pending.fk_second_user_id)    = LEAST(accepted.fk_first_user_id, accepted.fk_second_user_id)
     AND GREATEST(pending.fk_first_user_id, pending.fk_second_user_id) = GREATEST(accepted.fk_first_user_id, accepted.fk_second_user_id)
     AND pending.rival_linking_status  = 'PENDING'
     AND accepted.rival_linking_status = 'ACCEPTED'
);

-- Step 3: 동일 유저 쌍에 대해 PENDING/ACCEPTED 상태의 레코드가 중복 생성되는 것을 방지
-- LEAST/GREATEST로 방향(firstUser/secondUser)과 무관하게 정규화
CREATE UNIQUE INDEX uq_rivals_active_pair
ON rivals (
    LEAST(fk_first_user_id, fk_second_user_id),
    GREATEST(fk_first_user_id, fk_second_user_id)
)
WHERE rival_linking_status IN ('PENDING', 'ACCEPTED');