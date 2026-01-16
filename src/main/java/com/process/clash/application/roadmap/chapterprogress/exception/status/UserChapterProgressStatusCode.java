package com.process.clash.application.roadmap.chapterprogress.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserChapterProgressStatusCode implements StatusCode {

    USER_CHAPTER_PROGRESS_NOT_FOUND(
        "USER_CHAPTER_PROGRESS_NOT_FOUND",
        "챕터 진행 정보를 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
