package com.process.clash.adapter.web.common;

import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import com.process.clash.application.common.exception.exception.ApplicationException;
import com.process.clash.application.common.exception.mapper.HttpStatusMapper;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ApiResponse<Void> handleApplicationException(
            ApplicationException ex
    ) {
        StatusCode statusCode = ex.getStatusCode();
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode),
                httpStatus
        );
    }

    // Valid, RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMissingParams(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errors), httpStatus
        );
    }

    // RequestParam
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingRequestParam(
            MissingServletRequestParameterException ex) {

        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode),
                httpStatus
        );
    }

    // PathVariable, RequestParam 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode),
                httpStatus
        );
    }

    // Json 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {

        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode),
                httpStatus
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.error("Unhandled exception occurred", ex);

        StatusCode statusCode = CommonStatusCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode),
                httpStatus
        );
    }
}
