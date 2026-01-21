package com.process.clash.adapter.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class ApiResponse<T> extends ResponseEntity<CommonResponse<T>> {

    private ApiResponse(ResponseEntity<CommonResponse<T>> entity) {
        super(entity.getBody(), entity.getHeaders(), entity.getStatusCode());
    }

    // 1. 데이터만 포함된 성공 응답 (200 OK)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(CommonResponse.success(data, HttpStatus.OK));
    }

    // 2. 데이터와 메시지가 포함된 성공 응답 (200 OK)
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(CommonResponse.success(data, message, HttpStatus.OK));
    }

    // 3. 메시지만 포함된 성공 응답 (200 OK)
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(CommonResponse.success(message, HttpStatus.OK));
    }

    // 3. 메시지만 포함된 성공 응답 (200 OK)
    public static ApiResponse<Void> success(String message, ResponseCookie cookie) {
        return new ApiResponse<>(CommonResponse.success(message, cookie, HttpStatus.OK));
    }

    // 4. 생성 성공 응답 (201 Created)
    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(CommonResponse.success(data, message, HttpStatus.CREATED));
    }

    // 5. 생성 성공 응답 (201 Created)
    public static <T> ApiResponse<T> created(String message) {
        return new ApiResponse<>(CommonResponse.success(message, HttpStatus.CREATED));
    }

    // 6. 에러 응답 (GlobalExceptionHandler 등에서 사용)
    public static ApiResponse<Void> error(ErrorResponse error, HttpStatus status) {
        return new ApiResponse<>(CommonResponse.error(error, status));
    }
}