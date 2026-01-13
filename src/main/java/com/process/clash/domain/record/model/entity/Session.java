package com.process.clash.domain.record.model.entity;

import com.process.clash.domain.user.model.entity.User;
import java.time.LocalDateTime;

public record Session (
    Long id,
    User user,
    Task task,
    LocalDateTime startedAt,
    LocalDateTime endedAt
) {}
