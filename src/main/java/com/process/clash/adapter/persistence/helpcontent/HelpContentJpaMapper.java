package com.process.clash.adapter.persistence.helpcontent;

import com.process.clash.domain.helpcontent.entity.HelpContent;
import org.springframework.stereotype.Component;

@Component
public class HelpContentJpaMapper {

    public HelpContent toDomain(HelpContentJpaEntity entity) {
        return new HelpContent(
                entity.getKey(),
                entity.getContent(),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public HelpContentJpaEntity toJpaEntity(HelpContent domain) {
        return new HelpContentJpaEntity(
                domain.key(),
                domain.content(),
                domain.version(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }
}
