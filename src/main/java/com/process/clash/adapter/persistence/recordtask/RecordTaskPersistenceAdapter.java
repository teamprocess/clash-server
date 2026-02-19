package com.process.clash.adapter.persistence.recordtask;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.record.port.out.RecordTaskRepositoryPort;
import com.process.clash.domain.record.entity.RecordTask;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordTaskPersistenceAdapter implements RecordTaskRepositoryPort {

    private final RecordTaskJpaRepository recordTaskJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RecordTaskJpaMapper recordTaskJpaMapper;

    @Override
    public RecordTask save(RecordTask task) {
        UserJpaEntity user = userJpaRepository.getReferenceById(task.user().id());
        RecordTaskJpaEntity recordTaskJpaEntity = recordTaskJpaMapper.toJpaEntity(task, user);
        recordTaskJpaRepository.save(recordTaskJpaEntity);
        return recordTaskJpaMapper.toDomain(recordTaskJpaEntity);
    }

    @Override
    public Optional<RecordTask> findById(Long id) {
        return recordTaskJpaRepository.findById(id).map(recordTaskJpaMapper::toDomain);
    }

    @Override
    public List<RecordTask> findAllByUserId(Long userId) {
        return recordTaskJpaRepository.findAllByUserId(userId).stream()
            .map(recordTaskJpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        recordTaskJpaRepository.deleteById(id);
    }
}
