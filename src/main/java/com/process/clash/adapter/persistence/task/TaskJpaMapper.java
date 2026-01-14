package com.process.clash.adapter.persistence.task;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskJpaMapper {

    public static TaskJpaEntity toJpaEntity(Task task, UserJpaEntity user) {

        if (task.id() == null) {
            return TaskJpaEntity.create(
                task.name(),
                task.color(),
                user
            );
        }

        return TaskJpaEntity.from(
            task.id(),
            task.name(),
            task.color(),
            user,
            task.createdAt(),
            task.updatedAt()
        );
    }

    public static Task toDomain(TaskJpaEntity taskJpaEntity) {
        return new Task(
            taskJpaEntity.getId(),
            taskJpaEntity.getName(),
            taskJpaEntity.getColor(),
            0L,
            taskJpaEntity.getCreatedAt(),
            taskJpaEntity.getUpdatedAt(),
            UserJpaMapper.toDomain(taskJpaEntity.getUser())
        );
    }
}
