package com.process.clash.application.roadmap.category.exception.status;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryStatusCode implements StatusCode {

    CATEGORY_NOT_FOUND(
        "CATEGORY_NOT_FOUND",
        "카테고리를 찾을 수 없습니다.",
        ErrorCategory.NOT_FOUND
    ),

    CATEGORY_ALREADY_EXISTS(
        "CATEGORY_ALREADY_EXISTS",
        "이미 존재하는 카테고리 이름입니다.",
        ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;

}