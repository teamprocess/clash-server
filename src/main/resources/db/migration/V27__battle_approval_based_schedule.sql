-- 배틀 시작 기준을 신청일 기반 → 승인 시각 기반으로 변경
-- start_date / end_date (DATE) 제거
-- started_at (TIMESTAMPTZ, nullable) : 배틀 승인 시각 = 배틀 시작 시각
-- end_at     (TIMESTAMPTZ, nullable) : 배틀 종료 예정 시각 (started_at + duration)
-- duration   (INTEGER NOT NULL)      : 배틀 지속 일수

ALTER TABLE battles ADD COLUMN started_at TIMESTAMPTZ;
ALTER TABLE battles ADD COLUMN end_at     TIMESTAMPTZ;
ALTER TABLE battles ADD COLUMN duration   INTEGER;

-- 기존 데이터 마이그레이션: duration = end_date - start_date (일 단위)
UPDATE battles
SET duration = (end_date - start_date);

-- 기존 데이터 마이그레이션: PENDING / REJECTED 는 아직 승인 전이므로 시각 없음
-- 그 외 상태(NOT_STARTED, IN_PROGRESS, DONE, CANCELED)는 기존 날짜를 KST 자정 기준으로 변환
UPDATE battles
SET
    started_at = (start_date::text || 'T00:00:00+09:00')::timestamptz,
    end_at     = (end_date::text   || 'T00:00:00+09:00')::timestamptz
WHERE battle_status NOT IN ('PENDING', 'REJECTED');

-- duration NULL 방지 (혹시 start_date = end_date 이거나 예외 케이스)
UPDATE battles SET duration = 0 WHERE duration IS NULL;

ALTER TABLE battles ALTER COLUMN duration SET NOT NULL;

-- 기존 날짜 컬럼 제거
ALTER TABLE battles DROP COLUMN start_date;
ALTER TABLE battles DROP COLUMN end_date;