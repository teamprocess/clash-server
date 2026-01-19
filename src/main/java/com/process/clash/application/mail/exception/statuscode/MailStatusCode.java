package com.process.clash.application.mail.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailStatusCode implements StatusCode {

    // 500 계열: 시스템/설정 오류
    MAIL_SERVER_AUTHENTICATION(
            "MAIL_SERVER_AUTHENTICATION",
            "메일 서버 인증에 실패했습니다. 설정 정보를 확인해주세요.",
            ErrorCategory.INTERNAL_ERROR
    ),
    MAIL_CREATION_FAILED(
            "MAIL_CREATION_FAILED",
            "메일 메시지 생성 중 오류가 발생했습니다.",
            ErrorCategory.INTERNAL_ERROR
    ),
    MAIL_SEND_INTERRUPTED(
            "MAIL_SEND_INTERRUPTED",
            "메일 발송 중 서버 통신 오류가 발생했습니다.",
            ErrorCategory.INTERNAL_ERROR
    ),

    // 400 계열: 사용자/입력 데이터 오류
    INVALID_MAIL_ADDRESS(
            "INVALID_MAIL_ADDRESS",
            "올바르지 않은 이메일 주소 형식입니다.",
            ErrorCategory.BAD_REQUEST
    ),
    MAIL_RECIPIENT_REJECTED(
            "MAIL_RECIPIENT_REJECTED",
            "수신 거부되었거나 발송할 수 없는 이메일 주소입니다.",
            ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}