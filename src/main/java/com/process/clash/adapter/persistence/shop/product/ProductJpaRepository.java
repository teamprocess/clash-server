package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    // fetch join의 역할을 합니다
    @EntityGraph(attributePaths = "season")
    List<ProductJpaEntity> findTop10ByOrderByPopularityDesc();

    @EntityGraph(attributePaths = "season")
    Page<ProductJpaEntity> findByCategory(ProductCategory category, Pageable pageable);

    @EntityGraph(attributePaths = "season")
    Page<ProductJpaEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "season")
    List<ProductJpaEntity> findByIdIn(List<Long> ids);
}
