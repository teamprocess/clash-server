package com.process.clash.adapter.persistence.roadmap.category;

import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryJpaMapper {

    public CategoryJpaEntity toJpaEntity(Category category) {
        return new CategoryJpaEntity(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public Category toDomain(CategoryJpaEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}