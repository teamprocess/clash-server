package com.process.clash.adapter.persistence.major;

import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.internalserver.EntityRefreshFailedException;
import com.process.clash.domain.major.entity.MajorQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MajorQuestionPersistenceAdapter implements MajorQuestionRepositoryPort {

    private final MajorQuestionJpaRepository majorQuestionJpaRepository;
    private final MajorQuestionJpaMapper majorQuestionJpaMapper;

    @Override
    public MajorQuestion save(MajorQuestion majorQuestion) {
        MajorQuestionJpaEntity majorQuestionJpaEntity = majorQuestionJpaMapper.toJpaEntity(majorQuestion);
        MajorQuestionJpaEntity saved = majorQuestionJpaRepository.save(majorQuestionJpaEntity);
        majorQuestionJpaRepository.flush();

        // @UpdateTimestamp가 적용된 최신 데이터를 가져오기 위해 다시 조회
        MajorQuestionJpaEntity refreshed = majorQuestionJpaRepository.findById(saved.getId())
                .orElseThrow(EntityRefreshFailedException::new);

        return majorQuestionJpaMapper.toDomain(refreshed);
    }

    @Override
    public Optional<MajorQuestion> findById(Long id) {
        return majorQuestionJpaRepository.findById(id).map(majorQuestionJpaMapper::toDomain);
    }

    @Override
    public List<MajorQuestion> findAll() {
        return majorQuestionJpaRepository.findAll().stream()
                .map(majorQuestionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        majorQuestionJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return majorQuestionJpaRepository.existsById(id);
    }
}
