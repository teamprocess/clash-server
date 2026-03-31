package com.process.clash.application.announcement.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AnnouncementStatusCode implements StatusCode {

    ANNOUNCEMENT_NOT_FOUND("ANNOUNCEMENT_NOT_FOUND", "공지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
