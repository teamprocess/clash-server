package com.process.clash.application.user.userpomodorosetting.port.out;

import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;

public interface UserPomodoroSettingRepositoryPort {

    UserPomodoroSetting save(UserPomodoroSetting userPomodoroSetting);
}
