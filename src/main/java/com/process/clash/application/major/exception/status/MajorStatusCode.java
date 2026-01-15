package com.process.clash.application.major.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MajorStatusCode implements StatusCode {

    MAJOR_QUESTION_NOT_FOUND(
        "MAJOR_QUESTION_NOT_FOUND",
        "전공 질문을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
