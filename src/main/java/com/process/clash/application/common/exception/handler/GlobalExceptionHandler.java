package com.process.clash.application.common.exception.handler;

import com.process.clash.adapter.web.common.ErrorResponse;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import com.process.clash.application.common.exception.exception.ApplicationException;
import com.process.clash.application.common.exception.mapper.HttpStatusMapper;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex
    ) {
        StatusCode statusCode = ex.getStatusCode();
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.of(statusCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.error("Unhandled exception occurred", ex);

        StatusCode statusCode = CommonStatusCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.of(statusCode));
    }
}
