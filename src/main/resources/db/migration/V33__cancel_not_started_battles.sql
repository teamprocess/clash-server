-- 배틀 승인 즉시 IN_PROGRESS로 시작되도록 변경함에 따라
-- 기존에 남아 있는 NOT_STARTED 상태 배틀을 CANCELED로 일괄 전환
UPDATE battles
SET battle_status = 'CANCELED'
WHERE battle_status = 'NOT_STARTED';