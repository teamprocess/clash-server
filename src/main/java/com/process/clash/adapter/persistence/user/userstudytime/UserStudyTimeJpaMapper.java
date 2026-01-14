package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import org.springframework.stereotype.Component;

@Component
public class UserStudyTimeJpaMapper {

    public UserStudyTimeJpaEntity toJpaEntity(UserStudyTime userStudyTime) {
        return new UserStudyTimeJpaEntity(
                userStudyTime.id(),
                userStudyTime.date(),
                userStudyTime.userId()
        );
    }

    public UserStudyTime toDomain(UserStudyTimeJpaEntity userStudyTimeJpaEntity) {
        return new UserStudyTime(
                userStudyTimeJpaEntity.getId(),
                userStudyTimeJpaEntity.getDate(),
                userStudyTimeJpaEntity.getUserId()
        );
    }
}
