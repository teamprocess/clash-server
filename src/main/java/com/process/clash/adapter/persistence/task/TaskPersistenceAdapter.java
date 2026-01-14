package com.process.clash.adapter.persistence.task;

import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.model.entity.Task;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Task task) {
        TaskJpaEntity taskJpaEntity = TaskJpaMapper.toJpaEntity(task);
        taskJpaRepository.save(taskJpaEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id).map(TaskJpaMapper::toDomain);
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return taskJpaRepository.findAllByUserId(userId).stream()
            .map(TaskJpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }
}
