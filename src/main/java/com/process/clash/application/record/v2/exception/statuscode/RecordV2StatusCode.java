package com.process.clash.application.record.v2.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordV2StatusCode implements StatusCode {

    SUBJECT_V2_NOT_FOUND(
        "SUBJECT_V2_NOT_FOUND",
        "과목 그룹을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),
    TASK_V2_NOT_FOUND(
        "TASK_V2_NOT_FOUND",
        "세부 작업을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),
    ACTIVE_RECORD_SESSION_V2_NOT_FOUND(
        "ACTIVE_RECORD_SESSION_V2_NOT_FOUND",
        "진행 중인 기록 세션이 존재하지 않습니다.",
        ErrorCategory.NOT_FOUND
    ),
    RECORD_DEVELOP_SESSION_V2_NOT_FOUND(
        "RECORD_DEVELOP_SESSION_V2_NOT_FOUND",
        "개발 세션을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),
    RECORD_DEVELOP_SEGMENT_V2_NOT_FOUND(
        "RECORD_DEVELOP_SEGMENT_V2_NOT_FOUND",
        "개발 세그먼트를 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),
    RECORD_SESSION_V2_ALREADY_STARTED(
        "RECORD_SESSION_V2_ALREADY_STARTED",
        "이미 기록 세션이 시작되었습니다.",
        ErrorCategory.CONFLICT
    ),
    SUBJECT_V2_HAS_ACTIVE_SESSION(
        "SUBJECT_V2_HAS_ACTIVE_SESSION",
        "관련 기록이 있어 과목 그룹을 삭제할 수 없습니다.",
        ErrorCategory.CONFLICT
    ),
    TASK_V2_HAS_ACTIVE_SESSION(
        "TASK_V2_HAS_ACTIVE_SESSION",
        "관련 기록이 있어 세부 작업을 삭제할 수 없습니다.",
        ErrorCategory.CONFLICT
    ),
    SUBJECT_V2_ACCESS_DENIED(
        "SUBJECT_V2_ACCESS_DENIED",
        "해당 과목 그룹에 접근할 권한이 없습니다.",
        ErrorCategory.FORBIDDEN
    ),
    TASK_V2_ACCESS_DENIED(
        "TASK_V2_ACCESS_DENIED",
        "해당 세부 작업에 접근할 권한이 없습니다.",
        ErrorCategory.FORBIDDEN
    ),
    INVALID_RECORD_V2_START_REQUEST(
        "INVALID_RECORD_V2_START_REQUEST",
        "기록 시작 요청이 유효하지 않습니다.",
        ErrorCategory.BAD_REQUEST
    ),
    INVALID_DEVELOP_APP_SWITCH_REQUEST(
        "INVALID_DEVELOP_APP_SWITCH_REQUEST",
        "개발 앱 전환 요청이 유효하지 않습니다.",
        ErrorCategory.BAD_REQUEST
    ),
    INVALID_RECORD_V2_DAILY_DATE_REQUEST(
        "INVALID_RECORD_V2_DAILY_DATE_REQUEST",
        "요청한 조회 날짜가 유효하지 않습니다.",
        ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
