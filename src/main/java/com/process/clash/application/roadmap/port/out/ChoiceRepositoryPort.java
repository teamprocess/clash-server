package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.Choice;

import java.util.List;
import java.util.Optional;

public interface ChoiceRepositoryPort {
    void save(Choice choice);
    Optional<Choice> findById(Long id);
    List<Choice> findAll();
    List<Choice> findAllByQuestionId(Long questionId);
}
