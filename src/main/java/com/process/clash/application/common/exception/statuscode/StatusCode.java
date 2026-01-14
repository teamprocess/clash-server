package com.process.clash.application.common.exception.statuscode;

public interface StatusCode {
    String getCode();
    String getMessage();
    ErrorCategory getErrorCategory();
}
