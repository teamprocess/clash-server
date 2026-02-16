/*
 * Record timestamp normalization
 * - 목적: study_sessions / record_activity_segments 의 timestamp without time zone 컬럼을
 *   UTC 기준 timestamptz 로 정규화한다.
 *
 * 중요: 혼재 데이터(UTC 저장 vs Asia/Seoul 저장) 자동 판별이 불가능할 수 있다.
 * 아래 CUTOVER_TIMESTAMP 는 운영 배포 이력 기준으로 반드시 검토해야 한다.
 * 가정:
 *   - CUTOVER_TIMESTAMP 이전 레코드는 Asia/Seoul 로 저장되었고
 *   - CUTOVER_TIMESTAMP 이후 레코드는 UTC 로 저장되었다.
 */

DO $$
DECLARE
    CUTOVER_TIMESTAMP CONSTANT TIMESTAMP WITHOUT TIME ZONE := '${global_cutover_timestamp}'::timestamp;
BEGIN
    -- 1) 안전한 전환을 위해 신규 timestamptz 컬럼 추가
    ALTER TABLE study_sessions
        ADD COLUMN created_at_new TIMESTAMPTZ,
        ADD COLUMN updated_at_new TIMESTAMPTZ,
        ADD COLUMN started_at_new TIMESTAMPTZ,
        ADD COLUMN ended_at_new TIMESTAMPTZ;

    ALTER TABLE record_activity_segments
        ADD COLUMN created_at_new TIMESTAMPTZ,
        ADD COLUMN updated_at_new TIMESTAMPTZ,
        ADD COLUMN started_at_new TIMESTAMPTZ,
        ADD COLUMN ended_at_new TIMESTAMPTZ;

    -- 2) 데이터 보정 + 채우기 (cutover 기준 UTC/KST 분기)
    UPDATE study_sessions
    SET
        created_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN created_at AT TIME ZONE 'Asia/Seoul'
            ELSE created_at AT TIME ZONE 'UTC'
        END,
        updated_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN updated_at AT TIME ZONE 'Asia/Seoul'
            ELSE updated_at AT TIME ZONE 'UTC'
        END,
        started_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN started_at AT TIME ZONE 'Asia/Seoul'
            ELSE started_at AT TIME ZONE 'UTC'
        END,
        ended_at_new = CASE
            WHEN ended_at IS NULL THEN NULL
            WHEN created_at < CUTOVER_TIMESTAMP THEN ended_at AT TIME ZONE 'Asia/Seoul'
            ELSE ended_at AT TIME ZONE 'UTC'
        END;

    UPDATE record_activity_segments
    SET
        created_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN created_at AT TIME ZONE 'Asia/Seoul'
            ELSE created_at AT TIME ZONE 'UTC'
        END,
        updated_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN updated_at AT TIME ZONE 'Asia/Seoul'
            ELSE updated_at AT TIME ZONE 'UTC'
        END,
        started_at_new = CASE
            WHEN created_at < CUTOVER_TIMESTAMP THEN started_at AT TIME ZONE 'Asia/Seoul'
            ELSE started_at AT TIME ZONE 'UTC'
        END,
        ended_at_new = CASE
            WHEN ended_at IS NULL THEN NULL
            WHEN created_at < CUTOVER_TIMESTAMP THEN ended_at AT TIME ZONE 'Asia/Seoul'
            ELSE ended_at AT TIME ZONE 'UTC'
        END;

    -- 3) 제약/인덱스 정리 (스왑 전)
    DROP INDEX IF EXISTS uk_study_sessions_active_user;
    DROP INDEX IF EXISTS uk_record_activity_segments_open_session;
    ALTER TABLE record_activity_segments
        DROP CONSTRAINT IF EXISTS ck_record_activity_segments_time_range;

    -- 4) 컬럼 스왑 (old -> _legacy, new -> 원래 이름)
    ALTER TABLE study_sessions
        RENAME COLUMN created_at TO created_at_legacy;
    ALTER TABLE study_sessions
        RENAME COLUMN updated_at TO updated_at_legacy;
    ALTER TABLE study_sessions
        RENAME COLUMN started_at TO started_at_legacy;
    ALTER TABLE study_sessions
        RENAME COLUMN ended_at TO ended_at_legacy;

    ALTER TABLE study_sessions
        RENAME COLUMN created_at_new TO created_at;
    ALTER TABLE study_sessions
        RENAME COLUMN updated_at_new TO updated_at;
    ALTER TABLE study_sessions
        RENAME COLUMN started_at_new TO started_at;
    ALTER TABLE study_sessions
        RENAME COLUMN ended_at_new TO ended_at;

    ALTER TABLE record_activity_segments
        RENAME COLUMN created_at TO created_at_legacy;
    ALTER TABLE record_activity_segments
        RENAME COLUMN updated_at TO updated_at_legacy;
    ALTER TABLE record_activity_segments
        RENAME COLUMN started_at TO started_at_legacy;
    ALTER TABLE record_activity_segments
        RENAME COLUMN ended_at TO ended_at_legacy;

    ALTER TABLE record_activity_segments
        RENAME COLUMN created_at_new TO created_at;
    ALTER TABLE record_activity_segments
        RENAME COLUMN updated_at_new TO updated_at;
    ALTER TABLE record_activity_segments
        RENAME COLUMN started_at_new TO started_at;
    ALTER TABLE record_activity_segments
        RENAME COLUMN ended_at_new TO ended_at;

    -- 5) NOT NULL / DEFAULT / 제약 복구
    ALTER TABLE study_sessions
        ALTER COLUMN created_at SET NOT NULL,
        ALTER COLUMN updated_at SET NOT NULL,
        ALTER COLUMN started_at SET NOT NULL;
    ALTER TABLE study_sessions
        ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
    ALTER TABLE study_sessions
        ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;

    ALTER TABLE record_activity_segments
        ALTER COLUMN created_at SET NOT NULL,
        ALTER COLUMN updated_at SET NOT NULL,
        ALTER COLUMN started_at SET NOT NULL;
    ALTER TABLE record_activity_segments
        ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
    ALTER TABLE record_activity_segments
        ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;
    ALTER TABLE record_activity_segments
        ADD CONSTRAINT ck_record_activity_segments_time_range
            CHECK (ended_at IS NULL OR ended_at > started_at);

    -- 6) 레거시 컬럼 제거
    ALTER TABLE study_sessions
        DROP COLUMN created_at_legacy,
        DROP COLUMN updated_at_legacy,
        DROP COLUMN started_at_legacy,
        DROP COLUMN ended_at_legacy;

    ALTER TABLE record_activity_segments
        DROP COLUMN created_at_legacy,
        DROP COLUMN updated_at_legacy,
        DROP COLUMN started_at_legacy,
        DROP COLUMN ended_at_legacy;

    -- 7) 인덱스 복구
    CREATE UNIQUE INDEX uk_study_sessions_active_user
        ON study_sessions (fk_user_id)
        WHERE ended_at IS NULL;

    CREATE UNIQUE INDEX uk_record_activity_segments_open_session
        ON record_activity_segments (fk_study_session_id)
        WHERE ended_at IS NULL;
END $$;

-- 8) DB 레벨 방어: updated_at 자동 갱신 트리거
CREATE OR REPLACE FUNCTION set_updated_at_utc()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_study_sessions_set_updated_at ON study_sessions;
CREATE TRIGGER trg_study_sessions_set_updated_at
    BEFORE UPDATE ON study_sessions
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_utc();

DROP TRIGGER IF EXISTS trg_record_activity_segments_set_updated_at ON record_activity_segments;
CREATE TRIGGER trg_record_activity_segments_set_updated_at
    BEFORE UPDATE ON record_activity_segments
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at_utc();

/*
운영 검증 SQL (수동 실행)

-- 1) row count 확인
-- SELECT 'study_sessions' AS table_name, COUNT(*) FROM study_sessions
-- UNION ALL
-- SELECT 'record_activity_segments' AS table_name, COUNT(*) FROM record_activity_segments;

-- 2) UTC 저장 확인 (세션 타임존 무관, 내부 저장은 UTC instant)
-- SELECT id, created_at, updated_at, started_at, ended_at
-- FROM study_sessions
-- ORDER BY id DESC
-- LIMIT 20;

-- 3) 컷오버 근처 샘플 검증
-- SELECT id,
--        created_at,
--        created_at AT TIME ZONE 'Asia/Seoul' AS created_at_kst,
--        started_at,
--        started_at AT TIME ZONE 'Asia/Seoul' AS started_at_kst
-- FROM study_sessions
-- ORDER BY created_at
-- LIMIT 50;

-- 4) 아직 남은 timestamp without time zone 컬럼 점검(프로젝트 전체 확장용)
-- SELECT table_name, column_name
-- FROM information_schema.columns
-- WHERE table_schema = 'public'
--   AND data_type = 'timestamp without time zone'
-- ORDER BY table_name, ordinal_position;
*/
