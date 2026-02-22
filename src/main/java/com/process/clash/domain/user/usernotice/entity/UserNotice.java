package com.process.clash.domain.user.usernotice.entity;

import com.process.clash.domain.user.usernotice.enums.NoticeCategory;

import java.time.Instant;

public record UserNotice(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        NoticeCategory noticeCategory,
        boolean isRead,
        Long senderId,
        String senderName,
        String senderUsername,
        String senderProfileImage,
        Long receiverId,
        String receiverName,
        String receiverUsername,
        String receiverProfileImage
) {

    public static UserNotice createDefault(NoticeCategory noticeCategory, Long senderId, Long receiverId) {

        return new UserNotice(
                null,
                null,
                null,
                noticeCategory,
                false,
                senderId,
                null,
                null,
                null,
                receiverId,
                null,
                null,
                null
        );
    }

    public UserNotice markAsRead() {
        return new UserNotice(id, createdAt, updatedAt, noticeCategory, true, senderId, senderName, senderUsername, senderProfileImage, receiverId, receiverName, receiverUsername, receiverProfileImage);
    }
}
