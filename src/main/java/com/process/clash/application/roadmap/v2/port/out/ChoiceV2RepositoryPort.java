package com.process.clash.application.roadmap.v2.port.out;

import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;

import java.util.List;
import java.util.Optional;

public interface ChoiceV2RepositoryPort {
    ChoiceV2 save(ChoiceV2 choice);
    Optional<ChoiceV2> findById(Long id);
    List<ChoiceV2> findAllByQuestionId(Long questionId);
    void deleteById(Long id);
}
