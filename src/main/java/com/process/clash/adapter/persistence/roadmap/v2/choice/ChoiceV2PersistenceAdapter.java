package com.process.clash.adapter.persistence.roadmap.v2.choice;

import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaRepository;
import com.process.clash.application.roadmap.v2.port.out.ChoiceV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChoiceV2PersistenceAdapter implements ChoiceV2RepositoryPort {

    private final ChoiceV2JpaRepository choiceV2JpaRepository;
    private final ChoiceV2JpaMapper choiceV2JpaMapper;
    private final QuestionV2JpaRepository questionV2JpaRepository;

    @Override
    public ChoiceV2 save(ChoiceV2 choice) {
        QuestionV2JpaEntity questionEntity = questionV2JpaRepository.getReferenceById(choice.getQuestionId());
        ChoiceV2JpaEntity savedEntity = choiceV2JpaRepository.save(choiceV2JpaMapper.toJpaEntity(choice, questionEntity));
        return choiceV2JpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ChoiceV2> findById(Long id) {
        return choiceV2JpaRepository.findById(id).map(choiceV2JpaMapper::toDomain);
    }

    @Override
    public List<ChoiceV2> findAllByQuestionId(Long questionId) {
        return choiceV2JpaRepository.findAllByQuestionId(questionId).stream().map(choiceV2JpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        choiceV2JpaRepository.deleteById(id);
    }
}
