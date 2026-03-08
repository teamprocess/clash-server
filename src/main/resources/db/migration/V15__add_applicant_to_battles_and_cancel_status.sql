-- battles: 배틀 신청자 컬럼 추가 (취소 권한 확인용)
ALTER TABLE battles ADD COLUMN fk_applicant_id BIGINT;

ALTER TABLE battles
    ADD CONSTRAINT fk_battles_on_fk_applicant
    FOREIGN KEY (fk_applicant_id) REFERENCES users (id) ON DELETE SET NULL;
