package com.process.clash.application.user.userattendance.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserAttendanceStatusCode implements StatusCode {
    ALREADY_ATTENDED("ALREADY_ATTENDED", "오늘 이미 출석하였습니다.", HttpStatus.CONFLICT),
    USER_ATTENDANCE_NOT_FOUND("USER_ATTENDANCE_NOT_FOUND", "출석 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}