package com.process.clash.domain.group.entity;

import com.process.clash.domain.group.enums.GroupCategory;
import java.time.LocalDateTime;

public record Group (
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String name,
    String description,
    Integer maxMembers,
    String password,
    Boolean passwordRequired,
    GroupCategory category,
    Long ownerId
) {}
