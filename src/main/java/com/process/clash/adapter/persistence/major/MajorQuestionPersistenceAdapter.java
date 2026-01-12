package com.process.clash.adapter.persistence.major;

import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.major.MajorQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MajorQuestionPersistenceAdapter implements MajorQuestionRepositoryPort {

    private final MajorQuestionJpaRepository majorQuestionJpaRepository;

    @Override
    public void save(MajorQuestion majorQuestion) {
        MajorQuestionJpaEntity majorQuestionJpaEntity = MajorQuestionJpaMapper.toJpaEntity(majorQuestion);
        majorQuestionJpaRepository.save(majorQuestionJpaEntity);
    }

    @Override
    public Optional<MajorQuestion> findById(Long id) {
        return majorQuestionJpaRepository.findById(id).map(MajorQuestionJpaMapper::toDomain);
    }

    @Override
    public List<MajorQuestion> findAllByMajor(Major major) {
        return majorQuestionJpaRepository.findAllByMajor(major).stream()
                .map(MajorQuestionJpaMapper::toDomain)
                .toList();
    }
}
