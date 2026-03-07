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
                userNotice.updatedAt(),
                userNotice.noticeCategory(),
                userNotice.isRead(),
                sender,
                receiver,
                userNotice.rivalId(),
                userNotice.battleId(),
                userNotice.deletedAt()
        );
    }

    public UserNotice toDomain(UserNoticeJpaEntity userNoticeJpaEntity) {

        boolean isSelfNotice = userNoticeJpaEntity.getSender().getId()
                .equals(userNoticeJpaEntity.getReceiver().getId());

        String senderName = isSelfNotice ? "Clash" : userNoticeJpaEntity.getSender().getName();
        String senderUsername = isSelfNotice ? "clash" : userNoticeJpaEntity.getSender().getUsername();

        return new UserNotice(
                userNoticeJpaEntity.getId(),
                userNoticeJpaEntity.getCreatedAt(),
                userNoticeJpaEntity.getUpdatedAt(),
                userNoticeJpaEntity.getNoticeCategory(),
                userNoticeJpaEntity.isRead(),
                userNoticeJpaEntity.getSender().getId(),
                senderName,
                senderUsername,
                userNoticeJpaEntity.getSender().getProfileImage(),
                userNoticeJpaEntity.getReceiver().getId(),
                userNoticeJpaEntity.getReceiver().getName(),
                userNoticeJpaEntity.getReceiver().getUsername(),
                userNoticeJpaEntity.getReceiver().getProfileImage(),
                userNoticeJpaEntity.getRivalId(),
                userNoticeJpaEntity.getBattleId(),
                userNoticeJpaEntity.getDeletedAt()
        );
    }
}
