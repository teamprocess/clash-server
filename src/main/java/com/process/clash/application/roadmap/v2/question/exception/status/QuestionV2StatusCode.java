package com.process.clash.application.roadmap.v2.question.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionV2StatusCode implements StatusCode {

    CHAPTER_V2_NOT_FOUND(
            "CHAPTER_V2_NOT_FOUND",
            "챕터를 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    QUESTION_V2_NOT_FOUND(
            "QUESTION_V2_NOT_FOUND",
            "문제를 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    CHOICE_V2_NOT_FOUND(
            "CHOICE_V2_NOT_FOUND",
            "선택지를 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    CHAPTER_V2_LOCKED(
            "CHAPTER_V2_LOCKED",
            "챕터가 잠겨 있어 접근할 수 없습니다.",
            ErrorCategory.BAD_REQUEST
    ),

    INVALID_CHOICE_V2(
            "INVALID_CHOICE_V2",
            "유효하지 않은 선택지입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    INVALID_QUESTION_ORDER_V2(
            "INVALID_QUESTION_ORDER_V2",
            "문제를 순서대로 제출해야 합니다.",
            ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
