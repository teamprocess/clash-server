package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.MissionQuestion;

import java.util.List;
import java.util.Optional;

public interface MissionQuestionRepositoryPort {
    void save(MissionQuestion question);
    Optional<MissionQuestion> findById(Long id);
    List<MissionQuestion> findAll();
    List<MissionQuestion> findAllByMissionId(Long missionId);
}
