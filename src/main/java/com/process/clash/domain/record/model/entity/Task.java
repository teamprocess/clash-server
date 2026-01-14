package com.process.clash.domain.record.model.entity;

import com.process.clash.domain.record.model.enums.TaskColor;
import com.process.clash.domain.user.user.model.entity.User;
import java.time.LocalDateTime;

public record Task(
    Long id,
    String name,
    TaskColor color,
    Long studyTime, // ms
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    User user
) {
    public static Task create(String name, TaskColor color, User user) {
        return new Task(
            null,
            name,
            color,
            0L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            user
        );
    }
}
