package com.process.clash.adapter.persistence.user.userpomodorosetting;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPomodoroSettingPersistenceAdapter implements UserPomodoroSettingRepositoryPort {

    private final UserPomodoroSettingJpaMapper userPomodoroSettingJpaMapper;
    private final UserPomodoroSettingJpaRepository userPomodoroSettingJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserPomodoroSetting save(UserPomodoroSetting userPomodoroSetting) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userPomodoroSetting.userId());
        UserPomodoroSettingJpaEntity savedEntity = userPomodoroSettingJpaRepository
                .save(userPomodoroSettingJpaMapper.toJpaEntity(userPomodoroSetting, userJpaEntity));
        return userPomodoroSettingJpaMapper.toDomain(savedEntity);
    }
}
