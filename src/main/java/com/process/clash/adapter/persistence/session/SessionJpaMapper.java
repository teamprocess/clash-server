package com.process.clash.adapter.persistence.session;

import com.process.clash.domain.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SessionJpaMapper {

    public SessionJpaEntity toJpaEntity(User user) {
        return new SessionJpaEntity(
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

    public User toDomain(SessionJpaEntity sessionJpaEntity) {
        return new User(
                sessionJpaEntity.getId(),
                sessionJpaEntity.getCreatedAt(),
                sessionJpaEntity.getUpdatedAt(),
                sessionJpaEntity.getUsername(),
                sessionJpaEntity.getName(),
                sessionJpaEntity.getPassword(),
                sessionJpaEntity.getAbleToAddRival(),
                sessionJpaEntity.getProfileImage(),
                sessionJpaEntity.getPomodoroEnabled(),
                sessionJpaEntity.getPomodoroStudyMinute(),
                sessionJpaEntity.getPomodoroBreakMinute(),
                sessionJpaEntity.getMajor()
        );
    }
}