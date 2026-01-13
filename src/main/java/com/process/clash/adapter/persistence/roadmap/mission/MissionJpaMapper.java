package com.process.clash.adapter.persistence.roadmap.mission;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository;
import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaMapper;
import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaRepository;
import com.process.clash.domain.roadmap.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MissionJpaMapper {

    private final MissionQuestionJpaMapper missionQuestionJpaMapper;
    private final ChapterJpaRepository chapterJpaRepository;

    public MissionJpaEntity toJpaEntity(Mission mission) {
        return new MissionJpaEntity(
                mission.getId(),
                chapterJpaRepository.getReferenceById(mission.getChapterId()),
                mission.getTitle(),
                mission.getDifficulty(),
                Optional.ofNullable(mission.getQuestions()).orElse(Collections.emptyList())
                        .stream().map(missionQuestionJpaMapper::toJpaEntity).toList()
        );
    }

    public Mission toDomain(MissionJpaEntity entity) {
        return new Mission(
                entity.getId(),
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.getTitle(),
                entity.getDifficulty(),
                Optional.ofNullable(entity.getQuestions()).orElse(Collections.emptyList())
                        .stream().map(missionQuestionJpaMapper::toDomain).toList()
        );
    }
}
