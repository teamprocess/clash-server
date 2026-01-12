package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.roadmap.SectionKeyPoint;
import org.springframework.stereotype.Component;

@Component
public class SectionKeyPointJpaMapper {

    public SectionKeyPointJpaEntity toJpaEntity(SectionKeyPoint keyPoint) {
        SectionKeyPointJpaEntity e = keyPoint.getId() != null ? SectionKeyPointJpaEntity.ofId(keyPoint.getId()) : new SectionKeyPointJpaEntity();
        e.setContent(keyPoint.getContent());
        e.setOrderIndex(keyPoint.getOrderIndex());
        if (keyPoint.getSectionId() != null) {
            e.setSection(SectionJpaEntity.ofId(keyPoint.getSectionId()));
        }
        return e;
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
