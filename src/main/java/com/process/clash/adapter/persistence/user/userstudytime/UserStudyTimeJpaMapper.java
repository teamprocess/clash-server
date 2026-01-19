package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import org.springframework.stereotype.Component;

@Component
public class UserStudyTimeJpaMapper {

    public UserStudyTimeJpaEntity toJpaEntity(UserStudyTime userStudyTime, UserJpaEntity userJpaEntity) {
        return new UserStudyTimeJpaEntity(
                userStudyTime.id(),
                userStudyTime.date(),
                userStudyTime.totalStudyTimeSeconds(),
                userJpaEntity
        );
    }

    public UserStudyTime toDomain(UserStudyTimeJpaEntity userStudyTimeJpaEntity) {
        return new UserStudyTime(
                userStudyTimeJpaEntity.getId(),
                userStudyTimeJpaEntity.getDate(),
                userStudyTimeJpaEntity.getTotalStudyTimeSeconds(),
                userStudyTimeJpaEntity.getUser().getId()
        );
    }
}
