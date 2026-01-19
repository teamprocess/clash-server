package com.process.clash.application.user.user.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {

    // 400
    INVALID_TARGET_CATEGORY("INVALID_TARGET_CATEGORY", "잘못된 TargetCategory입니다.", ErrorCategory.BAD_REQUEST),
    INVALID_WEEK_CATEGORY("INVALID_WEEK_CATEGORY", "잘못된 WeekCategory입니다.", ErrorCategory.BAD_REQUEST),
    INVALID_PERIOD_CATEGORY("INVALID_PERIOD_CATEGORY", "잘못된 PeriodCategory입니다.", ErrorCategory.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL("VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL", "인증 코드가 만료되었거나 잘못된 이메일입니다.", ErrorCategory.BAD_REQUEST),
    VERIFICATION_CODE_MISMATCH("VERIFICATION_CODE_MISMATCH", "인증 코드가 일치하지 않습니다.", ErrorCategory.BAD_REQUEST),

    // 401
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "유효하지 않은 자격 증명입니다.", ErrorCategory.UNAUTHORIZED),
    NOT_AUTHENTICATED("NOT_AUTHENTICATED", "인증된 사용자가 아닙니다.", ErrorCategory.UNAUTHORIZED),
    UNVERIFIED_EMAIL("UNVERIFIED_EMAIL", "이메일이 인증되지 않앗습니다.", ErrorCategory.UNAUTHORIZED),

    // 403
    REQUIRED_ADMIN_ROLE("REQUIRED_ADMIN_ROLE", "어드민 권한이 요구됩니다.", ErrorCategory.FORBIDDEN),
    REQUIRED_USER_ROLE("REQUIRED_USER_ROLE", "로그인이 필요합니다.", ErrorCategory.FORBIDDEN),

    // 404
    USER_NOT_FOUND("USER_NOT_FOUND", "존재하지 않는 유저입니다.", ErrorCategory.NOT_FOUND),

    // 409
    USERNAME_ALREADY_EXIST("USERNAME_ALREADY_EXIST", "이미 존재하는 username입니다.", ErrorCategory.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}