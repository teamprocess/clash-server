package com.process.clash.adapter.persistence.major;

import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
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
    public void save(MajorQuestion majorQuestion) {
        MajorQuestionJpaEntity majorQuestionJpaEntity = majorQuestionJpaMapper.toJpaEntity(majorQuestion);
        majorQuestionJpaRepository.save(majorQuestionJpaEntity);
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
}
