-- user_notices soft delete 지원
ALTER TABLE user_notices ADD COLUMN deleted_at TIMESTAMPTZ NULL;
