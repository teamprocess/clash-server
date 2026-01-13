package com.process.clash.adapter.persistence.roadmap.keypoint;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.domain.roadmap.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SectionKeyPointJpaMapper {

    private final SectionJpaRepository sectionJpaRepository;

    public SectionKeyPointJpaEntity toJpaEntity(SectionKeyPoint keyPoint) {
        return new SectionKeyPointJpaEntity(
                keyPoint.getId(),
                sectionJpaRepository.getReferenceById(keyPoint.getSectionId()),
                keyPoint.getContent(),
                keyPoint.getOrderIndex()
        );
    }

    public SectionKeyPoint toDomain(SectionKeyPointJpaEntity entity) {
        return new SectionKeyPoint(
                entity.getId(),
                entity.getSection() != null ? entity.getSection().getId() : null,
                entity.getContent(),
                entity.getOrderIndex()
        );
    }
}
