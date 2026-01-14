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
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
