package com.process.clash.adapter.persistence.user.usernotice;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserNoticePersistenceAdapter implements UserNoticeRepositoryPort {

    private final UserNoticeJpaRepository userNoticeJpaRepository;
    private final UserNoticeJpaMapper userNoticeJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserNotice save(UserNotice userNotice) {

        UserJpaEntity sender = userJpaRepository.getReferenceById(userNotice.senderId());
        UserJpaEntity receiver = userJpaRepository.getReferenceById(userNotice.receiverId());
        UserNoticeJpaEntity savedEntity = userNoticeJpaRepository.save(userNoticeJpaMapper.toJpaEntity(userNotice, sender, receiver));
        return userNoticeJpaMapper.toDomain(savedEntity);
    }
}
