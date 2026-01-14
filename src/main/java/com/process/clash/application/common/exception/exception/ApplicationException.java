package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;

// 나중에 HttpStatus 별 예외들이 상속 받음 -> 모든 처리는 사실상 StatusCode가 담담 -> Http 의존성 최소화
@Getter
public abstract class ApplicationException extends RuntimeException {

    private final StatusCode statusCode;

    protected ApplicationException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }

    protected ApplicationException(StatusCode statusCode, Throwable cause) {
        super(statusCode.getMessage(), cause);
        this.statusCode = statusCode;
    }
}
