package com.process.clash.application.user.user.exception.statuscode;


import com.process.clash.application.common.exception.statuscode.StatusCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements StatusCode {

    // 400
    INVALID_TARGET_CATEGORY("INVALID_TARGET_CATEGORY", "잘못된 TargetCategory입니다.", HttpStatus.BAD_REQUEST),
    INVALID_WEEK_CATEGORY("INVALID_WEEK_CATEGORY", "잘못된 WeekCategory입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PERIOD_CATEGORY("INVALID_PERIOD_CATEGORY", "잘못된 PeriodCategory입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PROFILE_IMAGE_UPLOAD_REQUEST("INVALID_PROFILE_IMAGE_UPLOAD_REQUEST", "프로필 이미지 업로드 요청이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL("VERIFICATION_CODE_EXPIRED_OR_WRONG_EMAIL", "인증 코드가 만료되었거나 잘못된 이메일입니다.", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_MISMATCH("VERIFICATION_CODE_MISMATCH", "인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_RESET_TOKEN("INVALID_PASSWORD_RESET_TOKEN", "유효하지 않거나 만료된 비밀번호 재설정 링크입니다.", HttpStatus.BAD_REQUEST),

    // 401
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    NOT_AUTHENTICATED("NOT_AUTHENTICATED", "인증된 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED),
    UNVERIFIED_EMAIL("UNVERIFIED_EMAIL", "이메일이 인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),

    // 403
    REQUIRED_ADMIN_ROLE("REQUIRED_ADMIN_ROLE", "어드민 권한이 요구됩니다.", HttpStatus.FORBIDDEN),
    REQUIRED_USER_ROLE("REQUIRED_USER_ROLE", "로그인이 필요합니다.", HttpStatus.FORBIDDEN),

    // 404
    USER_NOT_FOUND("USER_NOT_FOUND", "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND),

    // 409
    USERNAME_ALREADY_EXIST("USERNAME_ALREADY_EXIST", "이미 존재하는 username입니다.", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXIST("EMAIL_ALREADY_EXIST", "이미 존재하는 email입니다.", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
