package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.Mission;

import java.util.List;
import java.util.Optional;

public interface MissionRepositoryPort {
    void save(Mission mission);
    Optional<Mission> findById(Long id);
    Optional<Mission> findByIdWithQuestions(Long id);
    List<Mission> findAll();
    List<Mission> findAllByChapterId(Long chapterId);
}
