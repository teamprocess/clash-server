package com.process.clash.adapter.persistence.user;

import com.process.clash.domain.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public UserJpaEntity toJpaEntity(User user) {
        return new UserJpaEntity(
                user.id(),
                user.createdAt(),
                user.updatedAt(),
                user.username(),
                user.name(),
                user.password(),
                user.ableToAddRival(),
                user.major()
        );
    }

    public User toDomain(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getCreatedAt(),
                userJpaEntity.getUpdatedAt(),
                userJpaEntity.getUsername(),
                userJpaEntity.getName(),
                userJpaEntity.getPassword(),
                userJpaEntity.getAbleToAddRival(),
                userJpaEntity.getMajor()
        );
    }
}