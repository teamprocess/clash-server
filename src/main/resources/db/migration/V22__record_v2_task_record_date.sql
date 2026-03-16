ALTER TABLE record_tasks_v2
    ADD COLUMN record_date DATE;

UPDATE record_tasks_v2
SET record_date = (
    (
        created_at AT TIME ZONE 'Asia/Seoul'
        - INTERVAL '6 hours'
    )::date
);

ALTER TABLE record_tasks_v2
    ALTER COLUMN record_date SET NOT NULL;

CREATE INDEX idx_record_tasks_v2_user_id_record_date
    ON record_tasks_v2 (fk_user_id, record_date);

CREATE INDEX idx_record_tasks_v2_subject_id_record_date
    ON record_tasks_v2 (fk_record_subject_id, record_date);
