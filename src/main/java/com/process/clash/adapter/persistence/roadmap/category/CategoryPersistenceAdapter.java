package com.process.clash.adapter.persistence.roadmap.category;

import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryJpaMapper categoryJpaMapper;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = categoryJpaMapper.toJpaEntity(category);
        CategoryJpaEntity savedEntity = categoryJpaRepository.save(entity);
        return categoryJpaMapper.toDomain(savedEntity);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
                .map(categoryJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id)
                .map(categoryJpaMapper::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name)
                .map(categoryJpaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}