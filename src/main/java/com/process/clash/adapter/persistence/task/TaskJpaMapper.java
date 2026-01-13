package com.process.clash.adapter.persistence.task;

import com.process.clash.domain.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskJpaMapper {

    public TaskJpaEntity toJpaEntity(User user) {
        return new TaskJpaEntity(
                user.id(),
                user.createdAt(),
                user.updatedAt(),
                user.username(),
                user.name(),
                user.password(),
                user.ableToAddRival(),
                user.profileImage(),
                user.pomodoroEnabled(),
                user.pomodoroStudyMinute(),
                user.pomodoroBreakMinute(),
                user.major()
        );
    }

    public User toDomain(TaskJpaEntity taskJpaEntity) {
        return new User(
                taskJpaEntity.getId(),
                taskJpaEntity.getCreatedAt(),
                taskJpaEntity.getUpdatedAt(),
                taskJpaEntity.getUsername(),
                taskJpaEntity.getName(),
                taskJpaEntity.getPassword(),
                taskJpaEntity.getAbleToAddRival(),
                taskJpaEntity.getProfileImage(),
                taskJpaEntity.getPomodoroEnabled(),
                taskJpaEntity.getPomodoroStudyMinute(),
                taskJpaEntity.getPomodoroBreakMinute(),
                taskJpaEntity.getMajor()
        );
    }
}