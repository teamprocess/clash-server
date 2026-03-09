ALTER TABLE user_notices ADD COLUMN requires_action BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE user_notices
SET requires_action = TRUE
WHERE notice_category IN ('APPLY_RIVAL', 'APPLY_BATTLE')
  AND deleted_at IS NULL;