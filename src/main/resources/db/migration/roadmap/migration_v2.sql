-- ============================================
-- Roadmap V2 마이그레이션 스크립트
-- ============================================
-- 이 스크립트는 v2 테이블을 생성하고 v1 테이블의 데이터를 마이그레이션합니다

-- ============================================
-- 단계 1: V2 테이블 생성
-- ============================================

-- 1.1 chapters_v2 테이블 생성
CREATE TABLE IF NOT EXISTS chapters_v2 (
    id BIGSERIAL PRIMARY KEY,
    fk_section_id BIGINT NOT NULL REFERENCES sections(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INTEGER NOT NULL,
    study_material_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_chapters_v2_section_id ON chapters_v2(fk_section_id);
CREATE INDEX IF NOT EXISTS idx_chapters_v2_order_index ON chapters_v2(order_index);

-- 1.2 questions_v2 테이블 생성
CREATE TABLE IF NOT EXISTS questions_v2 (
    id BIGSERIAL PRIMARY KEY,
    fk_chapter_id BIGINT NOT NULL REFERENCES chapters_v2(id) ON DELETE CASCADE,
    content VARCHAR(1000) NOT NULL,
    explanation TEXT,
    order_index INTEGER NOT NULL,
    difficulty INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_questions_v2_chapter_id ON questions_v2(fk_chapter_id);
CREATE INDEX IF NOT EXISTS idx_questions_v2_order_index ON questions_v2(order_index);

-- 1.3 choices_v2 테이블 생성
CREATE TABLE IF NOT EXISTS choices_v2 (
    id BIGSERIAL PRIMARY KEY,
    fk_question_id BIGINT NOT NULL REFERENCES questions_v2(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_choices_v2_question_id ON choices_v2(fk_question_id);

-- 1.4 user_question_history_v2 테이블 생성
CREATE TABLE IF NOT EXISTS user_question_history_v2 (
    id BIGSERIAL PRIMARY KEY,
    fk_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    fk_chapter_id BIGINT NOT NULL REFERENCES chapters_v2(id) ON DELETE CASCADE,
    is_cleared BOOLEAN NOT NULL DEFAULT FALSE,
    correct_count INTEGER NOT NULL DEFAULT 0,
    total_count INTEGER NOT NULL DEFAULT 0,
    current_question_index INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_chapter_v2 UNIQUE (fk_user_id, fk_chapter_id)
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_user_question_history_v2_user_id ON user_question_history_v2(fk_user_id);
CREATE INDEX IF NOT EXISTS idx_user_question_history_v2_chapter_id ON user_question_history_v2(fk_chapter_id);

-- ============================================
-- 단계 2: 챕터 마이그레이션 (1:1 복사)
-- ============================================
INSERT INTO chapters_v2 (fk_section_id, title, description, order_index, study_material_url, created_at, updated_at)
SELECT fk_section_id, title, description, order_index, NULL as study_material_url, created_at, updated_at
FROM chapters
ON CONFLICT DO NOTHING;

-- ============================================
-- 단계 3: 문제 마이그레이션 (Mission + MissionQuestion 병합)
-- ============================================
-- missions와 mission_questions를 questions_v2로 병합
-- orderIndex = (mission.orderIndex * 100) + missionQuestion.orderIndex
-- difficulty는 mission 테이블에서 가져옴

INSERT INTO questions_v2 (fk_chapter_id, content, explanation, order_index, difficulty, created_at, updated_at)
SELECT
    m.fk_chapter_id,
    mq.content,
    mq.explanation,
    (m.order_index * 100) + mq.order_index as order_index,  -- 결합된 순서 인덱스
    m.difficulty,
    mq.created_at,
    mq.updated_at
FROM mission_questions mq
INNER JOIN missions m ON mq.fk_mission_id = m.id
ORDER BY m.fk_chapter_id, (m.order_index * 100) + mq.order_index
ON CONFLICT DO NOTHING;

-- ============================================
-- 단계 4: 선택지 마이그레이션 (새로운 question_id 매핑)
-- ============================================
-- 동일한 로직을 사용하여 choices를 새로운 questions_v2에 매핑
-- content 일치를 기반으로 해당하는 question_v2.id를 찾아야 함

INSERT INTO choices_v2 (fk_question_id, content, is_correct, created_at, updated_at)
SELECT
    q2.id as fk_question_id,
    c.content,
    c.is_correct,
    c.created_at,
    c.updated_at
FROM choices c
INNER JOIN mission_questions mq ON c.fk_question_id = mq.id
INNER JOIN missions m ON mq.fk_mission_id = m.id
INNER JOIN questions_v2 q2 ON q2.fk_chapter_id = m.fk_chapter_id
    AND q2.order_index = (m.order_index * 100) + mq.order_index
    AND q2.content = mq.content  -- 정확한 매핑을 보장하기 위해 content로 일치시킴
ORDER BY c.id
ON CONFLICT DO NOTHING;

-- ============================================
-- 단계 5: 사용자 히스토리 마이그레이션 (챕터 레벨 집계)
-- ============================================
-- 미션 레벨 히스토리를 챕터 레벨로 집계
-- 챕터 내 모든 미션의 correctCount와 totalCount를 합산

INSERT INTO user_question_history_v2 (
    fk_user_id,
    fk_chapter_id,
    is_cleared,
    correct_count,
    total_count,
    current_question_index,
    created_at,
    updated_at
)
SELECT
    umh.fk_user_id,
    c2.id as fk_chapter_id,  -- 새로운 chapters_v2 테이블의 ID 사용
    -- 챕터 내 모든 미션이 클리어되었을 때만 챕터가 클리어됨
    BOOL_AND(umh.is_cleared) as is_cleared,
    -- 챕터 내 모든 미션의 정답 수 합계
    SUM(umh.correct_count) as correct_count,
    -- 챕터 내 모든 미션의 문제 수 합계
    SUM(umh.total_count) as total_count,
    -- 최대 current_question_index 사용 (또는 SUM 사용 가능)
    MAX(umh.current_question_index) as current_question_index,
    -- 가장 이른 생성 시간 사용
    MIN(umh.created_at) as created_at,
    -- 가장 최근 업데이트 시간 사용
    MAX(umh.updated_at) as updated_at
FROM user_mission_history umh
INNER JOIN missions m ON umh.fk_mission_id = m.id
INNER JOIN chapters c ON m.fk_chapter_id = c.id
INNER JOIN chapters_v2 c2 ON c2.fk_section_id = c.fk_section_id
    AND c2.title = c.title
    AND c2.order_index = c.order_index
GROUP BY umh.fk_user_id, c2.id
ON CONFLICT (fk_user_id, fk_chapter_id) DO UPDATE SET
    is_cleared = EXCLUDED.is_cleared,
    correct_count = EXCLUDED.correct_count,
    total_count = EXCLUDED.total_count,
    current_question_index = EXCLUDED.current_question_index,
    updated_at = EXCLUDED.updated_at;

-- ============================================
-- 단계 6: 검증 쿼리
-- ============================================
-- 마이그레이션을 검증하기 위해 이 쿼리들을 실행하세요

-- 챕터 개수 일치 확인
SELECT
    (SELECT COUNT(*) FROM chapters) as v1_chapters,
    (SELECT COUNT(*) FROM chapters_v2) as v2_chapters;

-- 문제 개수 일치 확인
SELECT
    (SELECT COUNT(*) FROM mission_questions) as v1_questions,
    (SELECT COUNT(*) FROM questions_v2) as v2_questions;

-- 선택지 개수 일치 확인
SELECT
    (SELECT COUNT(*) FROM choices) as v1_choices,
    (SELECT COUNT(*) FROM choices_v2) as v2_choices;

-- 샘플 챕터 비교
SELECT
    c.id as v1_id,
    c.title as v1_title,
    c2.id as v2_id,
    c2.title as v2_title
FROM chapters c
LEFT JOIN chapters_v2 c2 ON c.title = c2.title AND c.order_index = c2.order_index
LIMIT 10;

-- 샘플 문제 비교 (문제가 올바르게 병합되었는지 확인)
SELECT
    m.id as mission_id,
    m.title as mission_title,
    mq.id as v1_question_id,
    mq.content as v1_content,
    (m.order_index * 100) + mq.order_index as computed_order,
    q2.id as v2_question_id,
    q2.content as v2_content,
    q2.order_index as v2_order,
    q2.difficulty as v2_difficulty
FROM mission_questions mq
INNER JOIN missions m ON mq.fk_mission_id = m.id
LEFT JOIN questions_v2 q2 ON q2.fk_chapter_id = m.fk_chapter_id
    AND q2.order_index = (m.order_index * 100) + mq.order_index
LIMIT 10;

-- 사용자 히스토리 집계 확인
SELECT
    umh.fk_user_id,
    m.fk_chapter_id,
    COUNT(*) as mission_count,
    SUM(umh.correct_count) as total_correct,
    SUM(umh.total_count) as total_questions,
    uqh2.correct_count as v2_correct,
    uqh2.total_count as v2_total
FROM user_mission_history umh
INNER JOIN missions m ON umh.fk_mission_id = m.id
LEFT JOIN user_question_history_v2 uqh2 ON uqh2.fk_user_id = umh.fk_user_id
    AND uqh2.fk_chapter_id = m.fk_chapter_id
GROUP BY umh.fk_user_id, m.fk_chapter_id, uqh2.correct_count, uqh2.total_count
LIMIT 10;

-- ============================================
-- 참고사항:
-- ============================================
-- 1. 이 마이그레이션은 안전합니다 - v1 테이블을 삭제하거나 수정하지 않습니다
-- 2. 가능하면 트랜잭션 내에서 스크립트를 실행하세요 (BEGIN; ... COMMIT;)
-- 3. 먼저 스테이징 환경에서 테스트하세요
-- 4. V1 테이블 (chapters, missions, mission_questions, choices, user_mission_history)은
--    변경되지 않고 v1 API와 함께 계속 작동합니다
-- 5. 마이그레이션이 실패하면, v2 테이블을 삭제하고 다시 시도하세요
-- 6. 성공적인 마이그레이션과 테스트 후, v1 테이블은 최소 6개월 동안 유지할 수 있습니다
-- 7. ON CONFLICT DO NOTHING을 사용하여 스크립트를 멱등성 있게 만듭니다 (안전하게 여러 번 실행 가능)

-- ============================================
-- 롤백 계획:
-- ============================================
-- 롤백이 필요한 경우, v2 테이블을 삭제하기만 하면 됩니다:
-- DROP TABLE IF EXISTS user_question_history_v2 CASCADE;
-- DROP TABLE IF EXISTS choices_v2 CASCADE;
-- DROP TABLE IF EXISTS questions_v2 CASCADE;
-- DROP TABLE IF EXISTS chapters_v2 CASCADE;
-- V1 데이터는 손상되지 않으며 v1 API는 계속 작동합니다
