package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.entity.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    void deleteById(Long id);
}
