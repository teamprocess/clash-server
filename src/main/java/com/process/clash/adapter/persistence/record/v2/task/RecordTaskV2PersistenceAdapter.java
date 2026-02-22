package com.process.clash.adapter.persistence.record.v2.task;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaRepository;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordTaskV2PersistenceAdapter implements RecordTaskV2RepositoryPort {

    private final RecordTaskV2JpaRepository recordTaskV2JpaRepository;
    private final RecordSubjectV2JpaRepository recordSubjectV2JpaRepository;
    private final RecordTaskV2JpaMapper recordTaskV2JpaMapper;

    @Override
    public RecordTaskV2 save(RecordTaskV2 task) {
        if (task.id() == null) {
            RecordSubjectV2JpaEntity subject = recordSubjectV2JpaRepository.getReferenceById(task.subjectId());
            RecordTaskV2JpaEntity entity = RecordTaskV2JpaEntity.create(task.name(), subject);
            return recordTaskV2JpaMapper.toDomain(recordTaskV2JpaRepository.save(entity));
        }

        RecordTaskV2JpaEntity existing = recordTaskV2JpaRepository.findById(task.id())
            .orElseThrow(TaskV2NotFoundException::new);
        existing.changeName(task.name());
        return recordTaskV2JpaMapper.toDomain(recordTaskV2JpaRepository.save(existing));
    }

    @Override
    public Optional<RecordTaskV2> findById(Long id) {
        return recordTaskV2JpaRepository.findById(id)
            .map(recordTaskV2JpaMapper::toDomain);
    }

    @Override
    public Optional<RecordTaskV2> findByIdAndSubjectId(Long id, Long subjectId) {
        return recordTaskV2JpaRepository.findByIdAndSubjectId(id, subjectId)
            .map(recordTaskV2JpaMapper::toDomain);
    }

    @Override
    public List<RecordTaskV2> findAllBySubjectIds(Collection<Long> subjectIds) {
        if (subjectIds.isEmpty()) {
            return List.of();
        }
        return recordTaskV2JpaRepository.findAllBySubjectIdInOrderByCreatedAtAsc(subjectIds).stream()
            .map(recordTaskV2JpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        recordTaskV2JpaRepository.deleteById(id);
    }
}
