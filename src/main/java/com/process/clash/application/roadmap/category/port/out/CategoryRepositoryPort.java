package com.process.clash.application.roadmap.category.port.out;

import com.process.clash.domain.roadmap.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    void deleteById(Long id);
}