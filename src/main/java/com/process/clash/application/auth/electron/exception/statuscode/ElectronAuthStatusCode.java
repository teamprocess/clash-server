package com.process.clash.application.auth.electron.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElectronAuthStatusCode implements StatusCode {

    INVALID_REDIRECT_URI(
            "INVALID_REDIRECT_URI",
            "허용되지 않은 리다이렉트 URI입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    INVALID_STATE(
            "INVALID_STATE",
            "유효하지 않거나 만료된 state입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    RECAPTCHA_VERIFICATION_FAILED(
            "RECAPTCHA_VERIFICATION_FAILED",
            "Recaptcha 검증에 실패했습니다.",
            ErrorCategory.BAD_REQUEST
    ),

    INVALID_AUTH_CODE(
            "INVALID_AUTH_CODE",
            "유효하지 않거나 만료된 인증 코드입니다.",
            ErrorCategory.BAD_REQUEST
    ),

    STATE_MISMATCH(
            "STATE_MISMATCH",
            "state가 일치하지 않습니다.",
            ErrorCategory.BAD_REQUEST
    ),

    USER_NOT_FOUND_IN_AUTH(
            "USER_NOT_FOUND_IN_AUTH",
            "인증 코드에 해당하는 사용자를 찾을 수 없습니다.",
            ErrorCategory.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
