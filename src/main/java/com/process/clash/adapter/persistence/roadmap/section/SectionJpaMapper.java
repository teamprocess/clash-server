package com.process.clash.adapter.persistence.roadmap.section;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.domain.roadmap.entity.Section;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SectionJpaMapper {

    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;

    public SectionJpaEntity toJpaEntity(Section section) {
        SectionJpaEntity sectionEntity = new SectionJpaEntity(
                section.getId(),
                section.getMajor(),
                section.getTitle(),
                section.getDescription(),
                section.getCategory(),
                new ArrayList<>(),
                new ArrayList<>(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );

        // null-safe: section.getChapters()가 null이면 빈 리스트로 처리
        List<ChapterJpaEntity> chapters = (section.getChapters() != null)
                ? section.getChapters().stream()
                .map(c -> chapterJpaMapper.toEntity(c, sectionEntity)).toList() :
                new ArrayList<>();

        List<SectionKeyPointJpaEntity> keyPoints = (section.getKeyPoints() != null)
                ? section.getKeyPoints().stream().map(k -> sectionKeyPointJpaMapper.toJpaEntity(k, sectionEntity)).toList() :
                new ArrayList<>();

        sectionEntity.getChapters().addAll(chapters);
        sectionEntity.getKeyPoints().addAll(keyPoints);

        return sectionEntity;
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                // null-safe: entity.getChapters()가 null이면 빈 리스트로 처리
                (entity.getChapters() != null)
                        ? entity.getChapters().stream()
                        .map(chapterJpaMapper::toDomain).toList() :
                        new ArrayList<>(),
                (entity.getKeyPoints() != null)
                        ? entity.getKeyPoints().stream()
                        .map(sectionKeyPointJpaMapper::toDomain).toList() :
                        new ArrayList<>()
        );
    }
}