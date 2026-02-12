package com.process.clash.application.record.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordStatusCode implements StatusCode {

    TASK_NOT_FOUND(
        "TASK_NOT_FOUND",
        "과목을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),
    STUDY_SESSION_ALREADY_STARTED(
        "STUDY_SESSION_ALREADY_STARTED",
        "이미 공부 세션이 시작되었습니다.",
        ErrorCategory.CONFLICT
    ),
    TASK_HAS_ACTIVE_SESSION(
        "TASK_HAS_ACTIVE_SESSION",
        "진행 중인 공부 세션이 있어 과목을 삭제할 수 없습니다.",
        ErrorCategory.CONFLICT
    ),
    STUDY_SESSION_NOT_FOUND(
            "STUDY_SESSION_NOT_FOUND",
            "공부 세션이 존재하지 않습니다.",
            ErrorCategory.NOT_FOUND
    ),
    ACTIVE_STUDY_SESSION_NOT_FOUND(
            "ACTIVE_STUDY_SESSION_NOT_FOUND",
            "진행 중인 공부 세션이 존재하지 않습니다.",
            ErrorCategory.NOT_FOUND
    ),
    INVALID_RECORD_START_REQUEST(
        "INVALID_RECORD_START_REQUEST",
        "기록 시작 요청이 유효하지 않습니다.",
        ErrorCategory.BAD_REQUEST
    ),
    INVALID_MONITORED_APP(
        "INVALID_MONITORED_APP",
        "활동 기록 가능한 앱이 아닙니다.",
        ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
