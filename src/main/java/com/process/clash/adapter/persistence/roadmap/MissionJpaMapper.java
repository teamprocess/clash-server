package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.Mission;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MissionJpaMapper {

    private final MissionQuestionJpaMapper missionQuestionJpaMapper;

    public MissionJpaMapper(MissionQuestionJpaMapper missionQuestionJpaMapper) {
        this.missionQuestionJpaMapper = missionQuestionJpaMapper;
    }

    public MissionJpaEntity toJpaEntity(Mission mission) {
        MissionJpaEntity e = mission.getId() != null ? MissionJpaEntity.ofId(mission.getId()) : new MissionJpaEntity();
        e.setTitle(mission.getTitle());
        e.setDifficulty(mission.getDifficulty());
        if (mission.getChapterId() != null) {
            e.setChapter(ChapterJpaEntity.ofId(mission.getChapterId()));
        }
        if (mission.getQuestions() != null) {
            e.setQuestions(mission.getQuestions().stream().map(missionQuestionJpaMapper::toJpaEntity).toList());
        }
        return e;
    }

    public Mission toDomain(MissionJpaEntity entity) {
        return new Mission(
                entity.getId(),
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.getTitle(),
                entity.getDifficulty(),
                entity.getQuestions().stream().map(missionQuestionJpaMapper::toDomain).collect(Collectors.toList())
        );
    }
}
