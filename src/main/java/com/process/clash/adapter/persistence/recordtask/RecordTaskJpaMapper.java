package com.process.clash.adapter.persistence.recordtask;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.entity.RecordTask;
import org.springframework.stereotype.Component;

@Component
public class RecordTaskJpaMapper {

    private final UserJpaMapper userJpaMapper;

    public RecordTaskJpaMapper(UserJpaMapper userJpaMapper) {
        this.userJpaMapper = userJpaMapper;
    }

    public RecordTaskJpaEntity toJpaEntity(RecordTask task, UserJpaEntity user) {

        if (task.id() == null) {
            return RecordTaskJpaEntity.create(
                task.name(),
                user
            );
        }

        return RecordTaskJpaEntity.from(
            task.id(),
            task.name(),
            user,
            task.createdAt(),
            task.updatedAt()
        );
    }

    public RecordTask toDomain(RecordTaskJpaEntity recordTaskJpaEntity) {
        return new RecordTask(
            recordTaskJpaEntity.getId(),
            recordTaskJpaEntity.getName(),
            0L,
            recordTaskJpaEntity.getCreatedAt(),
            recordTaskJpaEntity.getUpdatedAt(),
            userJpaMapper.toDomain(recordTaskJpaEntity.getUser())
        );
    }
}
