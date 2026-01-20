package com.process.clash.adapter.persistence.user.userpomodorosetting;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import org.springframework.stereotype.Component;

@Component
public class UserPomodoroSettingJpaMapper {

    public UserPomodoroSettingJpaEntity toJpaEntity(UserPomodoroSetting userPomodoroSetting, UserJpaEntity userJpaEntity) {

        return new UserPomodoroSettingJpaEntity(
                userPomodoroSetting.id(),
                userPomodoroSetting.createdAt(),
                userPomodoroSetting.updatedAt(),
                userPomodoroSetting.pomodoroEnabled(),
                userPomodoroSetting.pomodoroStudyMinute(),
                userPomodoroSetting.pomodoroBreakMinute(),
                userJpaEntity
        );
    }

    public UserPomodoroSetting toDomain(UserPomodoroSettingJpaEntity userPomodoroSettingJpaEntity) {

        return new UserPomodoroSetting(
                userPomodoroSettingJpaEntity.getId(),
                userPomodoroSettingJpaEntity.getCreatedAt(),
                userPomodoroSettingJpaEntity.getUpdatedAt(),
                userPomodoroSettingJpaEntity.isPomodoroEnabled(),
                userPomodoroSettingJpaEntity.getPomodoroStudyMinute(),
                userPomodoroSettingJpaEntity.getPomodoroBreakMinute(),
                userPomodoroSettingJpaEntity.getUser().getId()
        );
    }
}
