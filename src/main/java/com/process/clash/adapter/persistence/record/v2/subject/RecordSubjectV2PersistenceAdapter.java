package com.process.clash.adapter.persistence.record.v2.subject;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordSubjectV2PersistenceAdapter implements RecordSubjectV2RepositoryPort {

    private final RecordSubjectV2JpaRepository recordSubjectV2JpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RecordSubjectV2JpaMapper recordSubjectV2JpaMapper;

    @Override
    public RecordSubjectV2 save(RecordSubjectV2 subject) {
        if (subject.id() == null) {
            UserJpaEntity user = userJpaRepository.getReferenceById(subject.userId());
            RecordSubjectV2JpaEntity entity = RecordSubjectV2JpaEntity.create(subject.name(), user);
            return recordSubjectV2JpaMapper.toDomain(recordSubjectV2JpaRepository.save(entity));
        }

        RecordSubjectV2JpaEntity existing = recordSubjectV2JpaRepository.findById(subject.id())
            .orElseThrow(SubjectV2NotFoundException::new);
        existing.changeName(subject.name());
        return recordSubjectV2JpaMapper.toDomain(recordSubjectV2JpaRepository.save(existing));
    }

    @Override
    public Optional<RecordSubjectV2> findById(Long id) {
        return recordSubjectV2JpaRepository.findById(id)
            .map(recordSubjectV2JpaMapper::toDomain);
    }

    @Override
    public Optional<RecordSubjectV2> findByIdAndUserId(Long id, Long userId) {
        return recordSubjectV2JpaRepository.findByIdAndUserId(id, userId)
            .map(recordSubjectV2JpaMapper::toDomain);
    }

    @Override
    public List<RecordSubjectV2> findAllByUserId(Long userId) {
        return recordSubjectV2JpaRepository.findAllByUserIdOrderByIdDesc(userId).stream()
            .map(recordSubjectV2JpaMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        recordSubjectV2JpaRepository.deleteById(id);
    }
}
