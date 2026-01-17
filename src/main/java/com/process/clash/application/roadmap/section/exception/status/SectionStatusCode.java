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
    ),

    SECTION_CIRCULAR_DEPENDENCY(
            "SECTION_CIRCULAR_DEPENDENCY",
            "로드맵은 본인을 선행 로드맵으로 지정할 수 없습니다.",
            ErrorCategory.UNPROCESSABLE_ENTITY
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
