package com.process.clash.adapter.persistence.task;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskJpaMapper {

    private final UserJpaMapper userJpaMapper;

    public TaskJpaMapper(UserJpaMapper userJpaMapper) {
        this.userJpaMapper = userJpaMapper;
    }

    public TaskJpaEntity toJpaEntity(Task task, UserJpaEntity user) {

        if (task.id() == null) {
            return TaskJpaEntity.create(
                task.name(),
                user
            );
        }

        return TaskJpaEntity.from(
            task.id(),
            task.name(),
            user,
            task.createdAt(),
            task.updatedAt()
        );
    }

    public Task toDomain(TaskJpaEntity taskJpaEntity) {
        return new Task(
            taskJpaEntity.getId(),
            taskJpaEntity.getName(),
            0L,
            taskJpaEntity.getCreatedAt(),
            taskJpaEntity.getUpdatedAt(),
            userJpaMapper.toDomain(taskJpaEntity.getUser())
        );
    }
}
