package com.process.clash.application.common.exception.statuscode;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
