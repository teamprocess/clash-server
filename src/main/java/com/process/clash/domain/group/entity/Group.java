package com.process.clash.domain.group.entity;

import com.process.clash.domain.group.enums.GroupCategory;
import com.process.clash.domain.user.user.entity.User;
import java.time.LocalDateTime;

public record Group (
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String name,
    String description,
    Integer maxMembers,
    Integer currentMemberCount,
    Boolean passwordRequired,
    GroupCategory category,
    User owner
) {

}
