package com.process.clash.application.record.v2.port.out;

import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordSessionV2RepositoryPort {

    RecordSessionV2 save(RecordSessionV2 session);

    Boolean existsActiveSessionByUserId(Long userId);

    Optional<RecordSessionV2> findActiveSessionByUserId(Long userId);

    Optional<RecordSessionV2> findActiveSessionByUserIdForUpdate(Long userId);

    List<RecordSessionV2> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    Boolean existsActiveSessionBySubjectId(Long subjectId);

    Boolean existsActiveSessionByTaskId(Long taskId);
}
