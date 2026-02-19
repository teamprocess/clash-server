package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.entity.RecordTask;
import java.util.List;
import java.util.Optional;

public interface RecordTaskRepositoryPort {
    RecordTask save(RecordTask task);
    Optional<RecordTask> findById(Long id);
    List<RecordTask> findAllByUserId(Long userId);
    void deleteById(Long id);
}
