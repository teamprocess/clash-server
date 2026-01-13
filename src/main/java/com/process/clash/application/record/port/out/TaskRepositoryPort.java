package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.model.entity.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    void save(Task session);
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
}
