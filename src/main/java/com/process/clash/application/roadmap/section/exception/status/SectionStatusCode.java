package com.process.clash.application.roadmap.section.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SectionStatusCode implements StatusCode {

    SECTION_NOT_FOUND(
        "SECTION_NOT_FOUND",
        "로드맵을 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
