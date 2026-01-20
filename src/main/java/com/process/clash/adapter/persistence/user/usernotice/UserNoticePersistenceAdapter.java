package com.process.clash.adapter.persistence.user.usernotice;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usernotice.exception.exception.badrequest.InvalidUserNoticeException;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public void saveAll(List<UserNotice> userNotices) {
        Set<Long> allUserIds = userNotices.stream()
                .flatMap(userNotice -> Stream.of(userNotice.senderId(), userNotice.receiverId()))
                .collect(Collectors.toSet());

        Map<Long, UserJpaEntity> userMap = userJpaRepository.findAllById(allUserIds)
                .stream()
                .collect(Collectors.toMap(UserJpaEntity::getId, user -> user));

        List<UserNoticeJpaEntity> entities = userNotices.stream()
                .map(userNotice -> {
                    if (userNotice.senderId() != null && userNotice.receiverId() != null) {
                        UserJpaEntity sender = userMap.get(userNotice.senderId());
                        UserJpaEntity receiver = userMap.get(userNotice.receiverId());
                        return userNoticeJpaMapper.toJpaEntity(userNotice, sender, receiver);
                    }

                    throw new InvalidUserNoticeException();
                })
                .toList();

        userNoticeJpaRepository.saveAll(entities);
    }
}
