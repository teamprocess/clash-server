package com.process.clash.adapter.persistence.roadmap.choice;

import com.process.clash.application.roadmap.port.out.ChoiceRepositoryPort;
import com.process.clash.domain.roadmap.entity.Choice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChoicePersistenceAdapter implements ChoiceRepositoryPort {

    private final ChoiceJpaRepository choiceJpaRepository;
    private final ChoiceJpaMapper choiceJpaMapper;
    private final com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaRepository missionQuestionJpaRepository;

    @Override
    public void save(Choice choice) {
        com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaEntity questionEntity = missionQuestionJpaRepository.getReferenceById(choice.getQuestionId());
        choiceJpaRepository.save(choiceJpaMapper.toJpaEntity(choice, questionEntity));
    }

    @Override
    public Optional<Choice> findById(Long id) {
        return choiceJpaRepository.findById(id).map(choiceJpaMapper::toDomain);
    }

    @Override
    public List<Choice> findAll() {
        return choiceJpaRepository.findAll().stream().map(choiceJpaMapper::toDomain).toList();
    }

    @Override
    public List<Choice> findAllByQuestionId(Long questionId) {
        return choiceJpaRepository.findAllByQuestionId(questionId).stream().map(choiceJpaMapper::toDomain).toList();
    }
}
