package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                section.getOrderIndex(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashSet<>(),
                section.getCreatedAt(), // createdAt
                section.getUpdatedAt()  // updatedAt
        );

        // null 안전성: section.getChapters()가 null이면 빈 리스트로 처리
        List<ChapterJpaEntity> chapters = (section.getChapters() != null)
                ? section.getChapters().stream()
                .map(c -> chapterJpaMapper.toEntity(c, sectionEntity)).toList() :
                new ArrayList<>();

        List<SectionKeyPointJpaEntity> keyPoints = (section.getKeyPoints() != null)
                ? section.getKeyPoints().stream().map(k -> sectionKeyPointJpaMapper.toJpaEntity(k, sectionEntity)).toList() :
                new ArrayList<>();

        sectionEntity.getChapters().addAll(chapters);
        sectionEntity.getKeyPoints().addAll(keyPoints);

        if (section.getPrerequisites() != null) {
            Set<SectionJpaEntity> prerequisites = section.getPrerequisites().stream()
                    .map(this::toPrerequisiteEntity) // 재귀 방지
                    .collect(Collectors.toSet());
            sectionEntity.getPrerequisites().addAll(prerequisites);
        }

        return sectionEntity;
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getOrderIndex(),
                // null 안전성: entity.getChapters()가 null이면 빈 리스트로 처리
                (entity.getChapters() != null)
                        ? entity.getChapters().stream()
                        .map(chapterJpaMapper::toDomain).toList() :
                        new ArrayList<>(),
                (entity.getKeyPoints() != null)
                        ? entity.getKeyPoints().stream()
                        .map(sectionKeyPointJpaMapper::toDomain).toList() :
                        new ArrayList<>(),
                (entity.getPrerequisites() != null)
                        ? entity.getPrerequisites().stream()
                        .map(this::toPrerequisiteDomain) // 깊이 제한 매핑 메서드 호출
                        .collect(Collectors.toSet())
                        : new HashSet<>(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Section toPrerequisiteDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getOrderIndex(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashSet<>(), // 선수 로드맵의 선수 로드맵은 더 이상 가져오지 않음
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private SectionJpaEntity toPrerequisiteEntity(Section domain) {
        return new SectionJpaEntity(
                domain.getId(),
                domain.getMajor(),
                domain.getTitle(),
                domain.getDescription(),
                domain.getCategory(),
                domain.getOrderIndex(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashSet<>(), // 여기도 비워둠
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}