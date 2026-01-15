package com.process.clash.adapter.persistence.user.usernotice;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import org.springframework.stereotype.Component;

@Component
public class UserNoticeJpaMapper {

    public UserNoticeJpaEntity toJpaEntity(UserNotice userNotice, UserJpaEntity sender, UserJpaEntity receiver) {

        return new UserNoticeJpaEntity(
                userNotice.id(),
                userNotice.createdAt(),
                userNotice.noticeCategory(),
                userNotice.isRead(),
                sender,
                receiver
        );
    }

    public UserNotice toDomain(UserNoticeJpaEntity userNoticeJpaEntity) {

        return new UserNotice(
                userNoticeJpaEntity.getId(),
                userNoticeJpaEntity.getCreationAt(),
                userNoticeJpaEntity.getNoticeCategory(),
                userNoticeJpaEntity.getIsRead(),
                userNoticeJpaEntity.getSender().getId(),
                userNoticeJpaEntity.getReceiver().getId()
        );
    }
}
