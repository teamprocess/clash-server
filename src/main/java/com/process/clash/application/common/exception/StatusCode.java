package com.process.clash.application.common.exception;

public interface StatusCode {
    String getCode();
    String getMessage();
    ErrorCategory getErrorCategory();
}
