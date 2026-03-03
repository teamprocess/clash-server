-- 기존 전체 범위 UNIQUE 제약 조건 제거
ALTER TABLE users DROP CONSTRAINT uc_users_username;
ALTER TABLE users DROP CONSTRAINT uc_users_email;

-- 활성 유저(deleted_at IS NULL)에만 적용되는 partial unique 인덱스 생성
-- 탈퇴한 유저(deleted_at IS NOT NULL)는 인덱스에서 제외되므로 동일 username/email로 재가입 가능
CREATE UNIQUE INDEX uc_users_username_active ON users (username) WHERE deleted_at IS NULL;
CREATE UNIQUE INDEX uc_users_email_active ON users (email) WHERE deleted_at IS NULL;
