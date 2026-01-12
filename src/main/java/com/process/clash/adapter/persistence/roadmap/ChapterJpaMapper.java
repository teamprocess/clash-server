package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.Chapter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ChapterJpaMapper {

    private final MissionJpaMapper missionJpaMapper;

    public ChapterJpaMapper(MissionJpaMapper missionJpaMapper) {
        this.missionJpaMapper = missionJpaMapper;
    }

    public ChapterJpaEntity toJpaEntity(Chapter chapter) {
        ChapterJpaEntity e = chapter.getId() != null ? ChapterJpaEntity.ofId(chapter.getId()) : new ChapterJpaEntity();
        e.setTitle(chapter.getTitle());
        e.setDescription(chapter.getDescription());
        e.setOrderIndex(chapter.getOrderIndex());
        if (chapter.getSectionId() != null) {
            e.setSection(SectionJpaEntity.ofId(chapter.getSectionId()));
        }
        if (chapter.getMissions() != null) {
            e.setMissions(chapter.getMissions().stream().map(missionJpaMapper::toJpaEntity).toList());
        }
        return e;
    }

    public Chapter toDomain(ChapterJpaEntity entity) {
        return new Chapter(
                entity.getId(),
                entity.getSection() != null ? entity.getSection().getId() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getOrderIndex(),
                entity.getMissions().stream().map(missionJpaMapper::toDomain).collect(Collectors.toList())
        );
    }
}
