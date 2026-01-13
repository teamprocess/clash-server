package com.process.clash.application.major.port.out;

import com.process.clash.domain.major.entity.MajorQuestion;

import java.util.List;
import java.util.Optional;

public interface MajorQuestionRepositoryPort {
    void save(MajorQuestion majorQuestion);
    Optional<MajorQuestion> findById(Long id);
    List<MajorQuestion> findAll();
}
