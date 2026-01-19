package com.process.clash.application.user.userstudytime.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStudyTimeStatusCode implements StatusCode {

    // 404
    USER_STUDY_TIME_NOT_FOUND("USER_STUDY_TIME_NOT_FOUND", "유저의 학습시간이 존재하지 않습니다.", ErrorCategory.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}