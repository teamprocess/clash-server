package com.process.clash.domain.user.usernotice.entity;

import com.process.clash.domain.user.usernotice.enums.NoticeCategory;

import java.time.LocalDateTime;

public record UserNotice(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        NoticeCategory noticeCategory,
        boolean isRead,
        Long senderId,
        Long receiverId
) {
}
