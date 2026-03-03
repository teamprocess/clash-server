-- record_tasks_v2: owner(user) 및 완료 상태 추가
ALTER TABLE record_tasks_v2
    ADD COLUMN fk_user_id BIGINT;

UPDATE record_tasks_v2 t
SET fk_user_id = s.fk_user_id
FROM record_subjects_v2 s
WHERE t.fk_record_subject_id = s.id;

ALTER TABLE record_tasks_v2
    ALTER COLUMN fk_user_id SET NOT NULL;

ALTER TABLE record_tasks_v2
    ADD CONSTRAINT fk_record_tasks_v2_user
        FOREIGN KEY (fk_user_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE INDEX idx_record_tasks_v2_user_id
    ON record_tasks_v2 (fk_user_id);

ALTER TABLE record_tasks_v2
    ADD COLUMN completed BOOLEAN NOT NULL DEFAULT FALSE;

-- record_tasks_v2: subject 연결 선택화
ALTER TABLE record_tasks_v2
    ALTER COLUMN fk_record_subject_id DROP NOT NULL;

ALTER TABLE record_tasks_v2 DROP CONSTRAINT fk_record_tasks_v2_subject;
ALTER TABLE record_tasks_v2
    ADD CONSTRAINT fk_record_tasks_v2_subject
        FOREIGN KEY (fk_record_subject_id) REFERENCES record_subjects_v2 (id) ON DELETE SET NULL;

-- record_task_sessions_v2: subject 기록 선택화
ALTER TABLE record_task_sessions_v2
    ALTER COLUMN fk_record_subject_id DROP NOT NULL;

ALTER TABLE record_task_sessions_v2 DROP CONSTRAINT fk_record_task_sessions_v2_subject;
ALTER TABLE record_task_sessions_v2
    ADD CONSTRAINT fk_record_task_sessions_v2_subject
        FOREIGN KEY (fk_record_subject_id) REFERENCES record_subjects_v2 (id) ON DELETE SET NULL;
