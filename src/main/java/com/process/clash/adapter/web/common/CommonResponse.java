package com.process.clash.adapter.web.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponse<T>(
        T data,
        Boolean success,
        String message,
        ErrorResponse error,
        @JsonIgnore
        Integer status
) {
    public static <T> ResponseEntity<CommonResponse<T>> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(CommonResponse.<T>builder()
                .data(data)
                .success(true)
                .message(null)
                .error(null)
                .status(status.value())
                .build());
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(CommonResponse.<T>builder()
                .data(null)
                .success(true)
                .message(message)
                .error(null)
                .status(status.value())
                .build());
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(String message, ResponseCookie cookie, HttpStatus status) {
        return ResponseEntity.status(status)
                .header("Set-Cookie", cookie.toString())
                .body(CommonResponse.<T>builder()
                .data(null)
                .success(true)
                .message(message)
                .error(null)
                .status(status.value())
                .build());
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(T data, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(CommonResponse.<T>builder()
                .data(data)
                .success(true)
                .message(message)
                .error(null)
                .status(status.value())
                .build());
    }

    public static ResponseEntity<CommonResponse<Void>> error(ErrorResponse error, HttpStatus status) {
        return ResponseEntity.status(status).body(CommonResponse.<Void>builder()
                .success(false)
                .error(error)
                .build());
    }
}