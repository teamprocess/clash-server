package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.domain.roadmap.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                section.getChapters().stream().map(chapterJpaMapper::toEntity).toList(),
                section.getKeyPoints().stream().map(sectionKeyPointJpaMapper::toJpaEntity).toList()
        );
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getChapters().stream().map(chapterJpaMapper::toDomain).toList(),
                entity.getKeyPoints().stream().map(sectionKeyPointJpaMapper::toDomain).toList()
        );
    }
}
