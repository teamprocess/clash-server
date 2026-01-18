package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaEntity;
import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaMapper;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SectionJpaMapper {

    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;
    private final CategoryJpaMapper categoryJpaMapper;
    private final SectionJpaRepository sectionJpaRepository;

    public SectionJpaEntity toJpaEntity(Section section) {
        SectionJpaEntity sectionEntity = new SectionJpaEntity(
                section.getId(),
                section.getMajor(),
                section.getTitle(),
                section.getDescription(),
                categoryJpaMapper.toJpaEntity(section.getCategory()),
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

        if (section.getPrerequisites() != null && !section.getPrerequisites().isEmpty()) {
            // DB에서 managed 엔티티를 가져와서 사용 (transient 엔티티 생성 방지)
            List<Long> prerequisiteIds = section.getPrerequisites().stream()
                    .map(Section::getId)
                    .toList();
            List<SectionJpaEntity> managedPrerequisites = sectionJpaRepository.findAllById(prerequisiteIds);
            sectionEntity.getPrerequisites().addAll(managedPrerequisites);
        }

        return sectionEntity;
    }

    public SectionJpaEntity toJpaEntity(Section section, Map<Long, CategoryJpaEntity> categoryMap) {
        CategoryJpaEntity categoryEntity = categoryMap.get(section.getCategory().getId());
        if (categoryEntity == null) {
            throw new RuntimeException("Category not found: " + section.getCategory().getId());
        }

        SectionJpaEntity sectionEntity = new SectionJpaEntity(
                section.getId(),
                section.getMajor(),
                section.getTitle(),
                section.getDescription(),
                categoryEntity,
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

        if (section.getPrerequisites() != null && !section.getPrerequisites().isEmpty()) {
            // DB에서 managed 엔티티를 가져와서 사용 (transient 엔티티 생성 방지)
            List<Long> prerequisiteIds = section.getPrerequisites().stream()
                    .map(Section::getId)
                    .toList();
            List<SectionJpaEntity> managedPrerequisites = sectionJpaRepository.findAllById(prerequisiteIds);
            sectionEntity.getPrerequisites().addAll(managedPrerequisites);
        }

        return sectionEntity;
    }

    public Section toDomain(SectionJpaEntity entity) {
        return new Section(
                entity.getId(),
                entity.getMajor(),
                entity.getTitle(),
                entity.getDescription(),
                categoryJpaMapper.toDomain(entity.getCategory()),
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
                categoryJpaMapper.toDomain(entity.getCategory()),
                entity.getOrderIndex(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashSet<>(), // 선수 로드맵의 선수 로드맵은 더 이상 가져오지 않음
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}