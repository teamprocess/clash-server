package com.process.clash.domain.user.usernotice.enums;

public enum NoticeCategory {
    APPLY_RIVAL("상대방이 라이벌을 신청했어요.", true),
    ACCEPT_RIVAL("상대방이 라이벌을 수락했어요.", false),
    REJECT_RIVAL("상대방이 라이벌을 거절했어요.", false),
    CANCEL_RIVAL("상대방이 라이벌 신청을 취소했어요.", false),
    APPLY_BATTLE("상대방이 배틀을 신청했어요.", true),
    ACCEPT_BATTLE("상대방이 배틀을 수락했어요.", false),
    REJECT_BATTLE("상대방이 배틀을 거절했어요.", false),
    CANCEL_BATTLE("상대방이 배틀 신청을 취소했어요.", false),
    GLOBAL_NOTICE("전체 공지사항이 도착했어요.", false);

    private final String message;
    private final boolean requiresAction;

    NoticeCategory(String message, boolean requiresAction) {
        this.message = message;
        this.requiresAction = requiresAction;
    }

    public String getMessage() {
        return message;
    }

    public boolean requiresAction() {
        return requiresAction;
    }
}