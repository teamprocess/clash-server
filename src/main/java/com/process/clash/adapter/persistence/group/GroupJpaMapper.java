package com.process.clash.adapter.persistence.group;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupJpaMapper {

    private final UserJpaMapper userJpaMapper;

    public GroupJpaEntity toJpaEntity(Group group) {

        return new GroupJpaEntity(
            group.id(),
            group.createdAt(),
            group.updatedAt(),
            group.name(),
            group.description(),
            group.maxMembers(),
            group.password(),
            group.passwordRequired(),
            group.category(),
            userJpaMapper.toJpaEntity(group.owner())
        );
    }

    public Group toDomain(GroupJpaEntity groupJpaEntity, Integer currentMemberCount) {

        return new Group(
                groupJpaEntity.getId(),
                groupJpaEntity.getCreatedAt(),
                groupJpaEntity.getUpdatedAt(),
                groupJpaEntity.getName(),
                groupJpaEntity.getDescription(),
                groupJpaEntity.getMaxMembers(),
                currentMemberCount,
                groupJpaEntity.getPassword(),
                groupJpaEntity.getPasswordRequired(),
                groupJpaEntity.getCategory(),
                userJpaMapper.toDomain(groupJpaEntity.getOwner())
        );
    }
}
