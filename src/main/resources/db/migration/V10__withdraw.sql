-- 회원탈퇴 지원
-- 유저 soft delete 컬럼 추가
ALTER TABLE users ADD COLUMN deleted_at TIMESTAMPTZ NULL;

-- battles.fk_rival_id: ON DELETE CASCADE → ON DELETE SET NULL
-- 탈퇴 시 라이벌 physical delete 후에도 배틀 이력 보존
ALTER TABLE battles ALTER COLUMN fk_rival_id DROP NOT NULL;

ALTER TABLE battles DROP CONSTRAINT FK_BATTLES_ON_FK_RIVAL;
ALTER TABLE battles
    ADD CONSTRAINT FK_BATTLES_ON_FK_RIVAL
    FOREIGN KEY (fk_rival_id) REFERENCES rivals (id) ON DELETE SET NULL;
