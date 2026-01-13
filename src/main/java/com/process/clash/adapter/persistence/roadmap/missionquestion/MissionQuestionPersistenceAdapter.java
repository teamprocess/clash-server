package com.process.clash.adapter.persistence.roadmap.missionquestion;

import com.process.clash.application.roadmap.port.out.MissionQuestionRepositoryPort;
import com.process.clash.domain.roadmap.MissionQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MissionQuestionPersistenceAdapter implements MissionQuestionRepositoryPort {

    private final MissionQuestionJpaRepository missionQuestionJpaRepository;
    private final MissionQuestionJpaMapper missionQuestionJpaMapper;

    @Override
    public void save(MissionQuestion question) {
        missionQuestionJpaRepository.save(missionQuestionJpaMapper.toJpaEntity(question));
    }

    @Override
    public Optional<MissionQuestion> findById(Long id) {
        return missionQuestionJpaRepository.findById(id).map(missionQuestionJpaMapper::toDomain);
    }

    @Override
    public List<MissionQuestion> findAll() {
        return missionQuestionJpaRepository.findAll().stream().map(missionQuestionJpaMapper::toDomain).toList();
    }

    @Override
    public List<MissionQuestion> findAllByMissionId(Long missionId) {
        return missionQuestionJpaRepository.findAllByMissionId(missionId).stream().map(missionQuestionJpaMapper::toDomain).toList();
    }
}
