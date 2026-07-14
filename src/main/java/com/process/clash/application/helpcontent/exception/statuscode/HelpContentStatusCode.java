package com.process.clash.application.helpcontent.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HelpContentStatusCode implements StatusCode {

    HELP_CONTENT_ALREADY_EXISTS("HELP_CONTENT_ALREADY_EXISTS", "이미 존재하는 도움말 키입니다.", HttpStatus.CONFLICT),
    HELP_CONTENT_NOT_FOUND("HELP_CONTENT_NOT_FOUND", "도움말 내용을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
