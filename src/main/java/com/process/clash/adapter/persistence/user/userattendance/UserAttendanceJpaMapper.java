package com.process.clash.adapter.persistence.user.userattendance;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import org.springframework.stereotype.Component;

@Component
public class UserAttendanceJpaMapper {

    public UserAttendanceJpaEntity toJpaEntity(UserAttendance userAttendance, UserJpaEntity userJpaEntity) {
        return new UserAttendanceJpaEntity(
                userAttendance.id(),
                userAttendance.createdAt(),
                userAttendance.updatedAt(),
                userJpaEntity,
                userAttendance.isAttended()
        );
    }

    public UserAttendance toDomain(UserAttendanceJpaEntity entity) {
        return new UserAttendance(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUser().getId(),
                entity.isAttended()
        );
    }
}