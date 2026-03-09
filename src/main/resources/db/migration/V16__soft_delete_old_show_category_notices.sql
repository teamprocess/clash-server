-- hotfix: SHOW_ACCEPT_RIVAL, SHOW_REJECT_RIVAL, SHOW_ACCEPT_BATTLE, SHOW_REJECT_BATTLE 카테고리 값이
-- NoticeCategory enum에서 제거되었으므로, DB에 남아있는 해당 레코드들을 soft delete 처리
UPDATE user_notices
SET deleted_at = now()
WHERE notice_category IN ('SHOW_ACCEPT_RIVAL', 'SHOW_REJECT_RIVAL', 'SHOW_ACCEPT_BATTLE', 'SHOW_REJECT_BATTLE')
  AND deleted_at IS NULL;