package com.process.clash.application.roadmap.sectionprogress.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSectionProgressStatusCode implements StatusCode {

    USER_SECTION_PROGRESS_NOT_FOUND(
        "USER_SECTION_PROGRESS_NOT_FOUND",
        "섹션 진행 정보를 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
