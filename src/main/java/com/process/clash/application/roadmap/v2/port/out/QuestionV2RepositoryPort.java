package com.process.clash.application.roadmap.v2.port.out;

import com.process.clash.domain.roadmap.v2.entity.QuestionV2;

import java.util.List;
import java.util.Optional;

public interface QuestionV2RepositoryPort {
    QuestionV2 save(QuestionV2 question);
    Optional<QuestionV2> findById(Long id);
    List<QuestionV2> findAllByChapterId(Long chapterId);
    void deleteById(Long id);
}
