package com.process.clash.adapter.persistence.roadmap.chapter;

import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaMapper;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.domain.roadmap.Chapter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChapterJpaMapper {

    private final MissionJpaMapper missionJpaMapper;
    private final SectionJpaRepository sectionJpaRepository;

    public ChapterJpaEntity toEntity(Chapter chapter) {
        return new ChapterJpaEntity(
                chapter.getId(),
                sectionJpaRepository.getReferenceById(chapter.getSectionId()),
                chapter.getTitle(),
                chapter.getDescription(),
                chapter.getOrderIndex(),
                Optional.ofNullable(chapter.getMissions()).orElse(Collections.emptyList())
                        .stream().map(missionJpaMapper::toJpaEntity).toList()
        );
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
