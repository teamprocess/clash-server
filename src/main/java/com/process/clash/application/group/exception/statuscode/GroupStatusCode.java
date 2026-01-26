package com.process.clash.application.group.exception.statuscode;

import com.process.clash.application.common.exception.statuscode.ErrorCategory;
import com.process.clash.application.common.exception.statuscode.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupStatusCode implements StatusCode {

    GROUP_NOT_FOUND(
        "GROUP_NOT_FOUND",
        "존재하지 않는 그룹입니다.",
        ErrorCategory.NOT_FOUND
    ),

    ALREADY_IN_THIS_GROUP(
        "ALREADY_IN_THIS_GROUP",
        "이미 그룹에 참여 중입니다.",
        ErrorCategory.CONFLICT
    ),

    GROUP_MEMBER_LIMIT_REACHED(
        "GROUP_MEMBER_LIMIT_REACHED",
        "그룹 인원이 가득 찼습니다.",
        ErrorCategory.CONFLICT
    ),

    GROUP_PASSWORD_REQUIRED(
        "GROUP_PASSWORD_REQUIRED",
        "그룹 참여에 비밀번호가 필요합니다.",
        ErrorCategory.BAD_REQUEST
    ),

    GROUP_PASSWORD_MISMATCH(
        "GROUP_PASSWORD_MISMATCH",
        "그룹 비밀번호가 일치하지 않습니다.",
        ErrorCategory.BAD_REQUEST
    ),

    GROUP_OWNER_CANNOT_QUIT(
        "GROUP_OWNER_CANNOT_QUIT",
        "그룹장은 탈퇴할 수 없습니다.",
        ErrorCategory.CONFLICT
    ),

    GROUP_MEMBER_LIMIT_TOO_SMALL(
        "GROUP_MEMBER_LIMIT_TOO_SMALL",
        "현재 인원보다 작은 최대 인원으로 변경할 수 없습니다.",
        ErrorCategory.BAD_REQUEST
    ),

    GROUP_NOT_MEMBER(
        "GROUP_NOT_MEMBER",
        "그룹에 참여하지 않았습니다.",
        ErrorCategory.BAD_REQUEST
    );

    private final String code;
    private final String message;
    private final ErrorCategory errorCategory;
}
