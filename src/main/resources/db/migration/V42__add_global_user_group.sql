-- 1. groups 테이블의 fk_user_id 컬럼을 nullable로 변경
ALTER TABLE groups ALTER COLUMN fk_user_id DROP NOT NULL;

-- 2. 전체 유저 그룹 생성 (owner 없음)
INSERT INTO groups (created_at, updated_at, name, description, max_members, password, password_required, category, fk_user_id)
VALUES (NOW(), NOW(), '전체 유저', '모든 클래시 유저가 자동으로 참여하는 그룹입니다.', 2147483647, '', false, 'GLOBAL', NULL);

-- 3. 기존 활성 유저 전원을 GLOBAL 그룹에 추가
INSERT INTO group_members (fk_group_id, fk_user_id)
SELECT (SELECT id FROM groups WHERE category = 'GLOBAL'), u.id
FROM users u
WHERE u.user_status = 'ACTIVE';
