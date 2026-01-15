package com.process.clash.adapter.web.common;

import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import com.process.clash.application.common.exception.exception.ApplicationException;
import com.process.clash.application.common.exception.exception.EndpointMovedException;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EndpointMovedException.class)
    public ApiResponse<Void> handleEndpointMovedException(
            EndpointMovedException ex
    ) {
        StatusCode statusCode = ex.getStatusCode();
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        Map<String, String> newEndpointInfo = new HashMap<>();
        newEndpointInfo.put("newEndpoint", ex.getNewEndpoint());

        return ApiResponse.error(
                ErrorResponse.of(statusCode, newEndpointInfo),
                httpStatus
        );
    }

    @ExceptionHandler(ApplicationException.class)
    public ApiResponse<Void> handleApplicationException(
            ApplicationException ex
    ) {
        Map<String, String> errors = createErrorMap(ex.getMessage());
        StatusCode statusCode = ex.getStatusCode();
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errors),
                httpStatus
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoResourceFoundException(
            NoResourceFoundException ex) {
        log.warn("Endpoint not found", ex);
        StatusCode statusCode = CommonStatusCode.ENDPOINT_NOT_FOUND;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode), httpStatus
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        log.warn("Http request method not supported: {}", ex.getMethod());

        Map<String, String> errorMethod = new HashMap<>();
        errorMethod.put("method", ex.getMethod());
        StatusCode statusCode = CommonStatusCode.METHOD_NOT_ALLOWED;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errorMethod), httpStatus
        );
    }

    // Valid, RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMissingParams(
            MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "유효하지 않은 값입니다."
                ));
        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errors), httpStatus
        );
    }

    // RequestParam, PathVariable, RequestBody 등 형식 관련 예외 처리
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleInvalidArgumentExceptions(Exception ex) {
        log.warn("Invalid argument exception: {}", ex.getMessage());
        Map<String, String> errors = createErrorMap(ex.getMessage());
        StatusCode statusCode = CommonStatusCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errors),
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

        Map<String, String> errors = createErrorMap(ex.getMessage());
        StatusCode statusCode = CommonStatusCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpStatus = HttpStatusMapper.toHttpStatus(statusCode);

        return ApiResponse.error(
                ErrorResponse.of(statusCode, errors),
                httpStatus
        );
    }

    private Map<String, String> createErrorMap(String message) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", message);
        return errors;
    }
}
