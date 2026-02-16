package com.process.clash.adapter.persistence.roadmap.mission;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaMapper;
import com.process.clash.domain.roadmap.entity.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionJpaMapper {

    private final MissionQuestionJpaMapper missionQuestionJpaMapper;

    public MissionJpaEntity toJpaEntity(Mission mission, ChapterJpaEntity chapterEntity) {
        MissionJpaEntity missionEntity = new MissionJpaEntity(
                mission.getId(),
                chapterEntity,
                mission.getTitle(),
                mission.getDifficulty(),
                mission.getOrderIndex(),
                new ArrayList<>(),
                null, // createdAt은 @CreatedDate가 자동으로 설정
                null  // updatedAt은 @LastModifiedDate가 자동으로 설정
        );

        // null-safe: mission.getQuestions()가 null이면 빈 리스트로 처리
        List<MissionQuestionJpaEntity> questions = (mission.getQuestions() != null)
                ? mission.getQuestions().stream()
                .map(q -> missionQuestionJpaMapper.toJpaEntity(q, missionEntity)).toList() :
                new ArrayList<>();

        missionEntity.getQuestions().addAll(questions);

        return missionEntity;
    }

    public Mission toDomain(MissionJpaEntity entity) {
        return new Mission(
                entity.getId(),
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.getTitle(),
                entity.getDifficulty(),
                entity.getOrderIndex(),
                // null-safe: entity.getQuestions()가 null이면 빈 리스트로 처리
                (entity.getQuestions() != null)
                        ? entity.getQuestions().stream()
                        .map(missionQuestionJpaMapper::toDomain).toList() :
                        new ArrayList<>()
        );
    }
}