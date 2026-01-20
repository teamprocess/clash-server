package com.process.clash.application.user.userpomodorosetting.port.out;

import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;

import java.util.Optional;

public interface UserPomodoroSettingRepositoryPort {

    UserPomodoroSetting save(UserPomodoroSetting userPomodoroSetting);
    Optional<UserPomodoroSetting> findByUserId(Long userId);
}
