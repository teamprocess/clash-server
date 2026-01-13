package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.domain.roadmap.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SectionJpaMapper {

    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;

    public SectionJpaEntity toJpaEntity(Section section) {
        return new SectionJpaEntity(
                section.getId(),
                section.getMajor(),
                section.getTitle(),
                section.getDescription(),
                section.getCategory(),
                Optional.ofNullable(section.getChapters()).orElse(Collections.emptyList())
                        .stream().map(chapterJpaMapper::toEntity).toList(),
                Optional.ofNullable(section.getKeyPoints()).orElse(Collections.emptyList())
                        .stream().map(sectionKeyPointJpaMapper::toJpaEntity).toList()
        );
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                Optional.ofNullable(entity.getChapters()).orElse(Collections.emptyList())
                        .stream().map(chapterJpaMapper::toDomain).toList(),
                Optional.ofNullable(entity.getKeyPoints()).orElse(Collections.emptyList())
                        .stream().map(sectionKeyPointJpaMapper::toDomain).toList()
        );
    }
}
