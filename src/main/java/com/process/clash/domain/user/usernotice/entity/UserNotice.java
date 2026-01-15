package com.process.clash.domain.user.usernotice.entity;

import com.process.clash.domain.user.usernotice.enums.NoticeCategory;

import java.time.LocalDateTime;

public record UserNotice(
        Long id,
        LocalDateTime createdAt,
        NoticeCategory noticeCategory,
        Boolean isRead,
        Long senderId,
        Long receiverId
) {
}
