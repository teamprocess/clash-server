package com.process.clash.adapter.persistence.roadmap.chapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaMapper;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.domain.roadmap.entity.Chapter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChapterJpaMapper {

    private final MissionJpaMapper missionJpaMapper;

    public ChapterJpaEntity toEntity(Chapter chapter, SectionJpaEntity sectionEntity) {
        // 먼저 빈 missions 리스트로 Chapter 엔티티 생성
        ChapterJpaEntity chapterEntity = new ChapterJpaEntity(
                chapter.getId(),
                sectionEntity,
                chapter.getTitle(),
                chapter.getDescription(),
                chapter.getOrderIndex(),
                new ArrayList<>()
        );

        List<MissionJpaEntity> missions = Optional.ofNullable(chapter.getMissions()).orElse(Collections.emptyList())
                .stream().map(m -> missionJpaMapper.toJpaEntity(m, chapterEntity)).toList();

        // missions 리스트를 기존의 리스트에 추가
        chapterEntity.getMissions().addAll(missions);

        return chapterEntity;
    }

    public Chapter toDomain(ChapterJpaEntity entity) {
        return new Chapter(
                entity.getId(),
                entity.getSection() != null ? entity.getSection().getId() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getOrderIndex(),
                Optional.ofNullable(entity.getMissions()).orElse(Collections.emptyList())
                        .stream().map(missionJpaMapper::toDomain).collect(Collectors.toList())
        );
    }
}
