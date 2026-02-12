ALTER TABLE study_sessions
    ALTER COLUMN fk_task_id DROP NOT NULL;

ALTER TABLE study_sessions
    ADD COLUMN record_type VARCHAR(20);

ALTER TABLE study_sessions
    ADD COLUMN app_name VARCHAR(255);

UPDATE study_sessions
SET record_type = 'TASK'
WHERE record_type IS NULL;

ALTER TABLE study_sessions
    ALTER COLUMN record_type SET NOT NULL;

ALTER TABLE study_sessions
    ADD CONSTRAINT ck_study_sessions_record_type
        CHECK (record_type IN ('TASK', 'ACTIVITY'));

ALTER TABLE study_sessions
    ADD CONSTRAINT ck_study_sessions_record_payload
        CHECK (
            (record_type = 'TASK' AND fk_task_id IS NOT NULL AND app_name IS NULL)
                OR
            (record_type = 'ACTIVITY' AND fk_task_id IS NULL AND app_name IS NOT NULL)
            );
