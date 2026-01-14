package com.process.clash.adapter.persistence.roadmap.keypoint;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SectionKeyPointJpaMapper {

    public SectionKeyPointJpaEntity toJpaEntity(SectionKeyPoint keyPoint, SectionJpaEntity sectionEntity) {
        return new SectionKeyPointJpaEntity(
                keyPoint.getId(),
                sectionEntity,
                keyPoint.getContent(),
                keyPoint.getOrderIndex(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
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
