package com.process.clash.domain.group.entity;

import com.process.clash.domain.group.enums.GroupCategory;
import java.time.Instant;

public record Group (
    Long id,
    Instant createdAt,
    Instant updatedAt,
    String name,
    String description,
    Integer maxMembers,
    String password,
    Boolean passwordRequired,
    GroupCategory category,
    Long ownerId
) {

    public static Group createDefault(
        String name,
        String description,
        Integer maxMembers,
        String password,
        Boolean passwordRequired,
        GroupCategory category,
        Long ownerId
    ) {
        return new Group(
            null,
            null,
            null,
            name,
            description,
            maxMembers,
            password,
            passwordRequired,
            category,
            ownerId
        );
    }
}
