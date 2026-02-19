package com.process.clash.adapter.persistence.recordsession;

import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaEntity;
import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.enums.RecordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordSessionJpaMapper {

    private final RecordTaskJpaMapper recordTaskJpaMapper;
    private final UserJpaMapper userJpaMapper;

    public RecordSessionJpaEntity toJpaEntity(RecordSession recordSession, UserJpaEntity user, RecordTaskJpaEntity task) {
        return RecordSessionJpaEntity.create(
            user,
            task,
            recordSession.recordType(),
            recordSession.appId(),
            recordSession.startedAt()
        );
    }

    public RecordSession toDomain(RecordSessionJpaEntity recordSessionJpaEntity) {
        RecordType recordType = recordSessionJpaEntity.getRecordType();

        return RecordSession.create(
            recordSessionJpaEntity.getId(),
            userJpaMapper.toDomain(recordSessionJpaEntity.getUser()),
            recordSessionJpaEntity.getTask() == null
                ? null
                : recordTaskJpaMapper.toDomain(recordSessionJpaEntity.getTask()),
            recordType,
            recordSessionJpaEntity.getAppId(),
            recordSessionJpaEntity.getStartedAt(),
            recordSessionJpaEntity.getEndedAt()
        );
    }
}
