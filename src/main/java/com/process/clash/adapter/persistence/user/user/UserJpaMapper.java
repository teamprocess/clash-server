package com.process.clash.adapter.persistence.user.user;

import com.process.clash.domain.user.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public UserJpaEntity toJpaEntity(User user) {
        return new UserJpaEntity(
                user.id(),
                user.createdAt(),
                user.updatedAt(),
                user.username(),
                user.email(),
                user.name(),
                user.password(),
                user.role(),
                user.profileImage(),
                user.totalExp(),
                user.totalCookie(),
                user.major(),
                user.userStatus(),
                null
        );
    }

    public User toDomain(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getCreatedAt(),
                userJpaEntity.getUpdatedAt(),
                userJpaEntity.getUsername(),
                userJpaEntity.getEmail(),
                userJpaEntity.getName(),
                userJpaEntity.getPassword(),
                userJpaEntity.getRole(),
                userJpaEntity.getProfileImage(),
                userJpaEntity.getTotalExp(),
                userJpaEntity.getTotalCookie(),
                userJpaEntity.getMajor(),
                userJpaEntity.getUserStatus(),
                userJpaEntity.getDeletedAt()
        );
    }
}
