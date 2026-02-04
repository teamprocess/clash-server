package com.process.clash.application.roadmap.missions.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatusCode implements StatusCode {

    MISSION_NOT_FOUND(
            "MISSION_NOT_FOUND",
            "미션을 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    QUESTION_NOT_FOUND(
            "QUESTION_NOT_FOUND",
            "질문을 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    INVALID_CHOICE(
            "INVALID_CHOICE",
            "유효하지 않은 선택지입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    CHAPTER_LOCKED(
            "CHAPTER_LOCKED",
            "챕터가 잠겨 있어 접근할 수 없습니다.",
            ErrorCategory.BAD_REQUEST
    ),

    CHAPTER_NOT_FOUND(
            "CHAPTER_NOT_FOUND",
            "챕터를 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    ),

    INVALID_QUESTION_ORDER(
            "INVALID_QUESTION_ORDER",
            "문제를 순서대로 제출해야 합니다.",
            ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}