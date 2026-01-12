package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.Section;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SectionJpaMapper {

    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;

    public SectionJpaMapper(ChapterJpaMapper chapterJpaMapper, SectionKeyPointJpaMapper sectionKeyPointJpaMapper) {
        this.chapterJpaMapper = chapterJpaMapper;
        this.sectionKeyPointJpaMapper = sectionKeyPointJpaMapper;
    }

    public SectionJpaEntity toJpaEntity(Section section) {
        SectionJpaEntity e = section.getId() != null ? SectionJpaEntity.ofId(section.getId()) : new SectionJpaEntity();
        e.setMajor(section.getMajor());
        e.setTitle(section.getTitle());
        e.setDescription(section.getDescription());
        e.setCategory(section.getCategory());
        if (section.getChapters() != null) {
            e.setChapters(section.getChapters().stream().map(chapterJpaMapper::toJpaEntity).toList());
        }
        if (section.getKeyPoints() != null) {
            e.setKeyPoints(section.getKeyPoints().stream().map(sectionKeyPointJpaMapper::toJpaEntity).toList());
        }
        return e;
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getChapters().stream().map(chapterJpaMapper::toDomain).collect(Collectors.toList()),
                entity.getKeyPoints().stream().map(sectionKeyPointJpaMapper::toDomain).collect(Collectors.toList())
        );
    }
}
