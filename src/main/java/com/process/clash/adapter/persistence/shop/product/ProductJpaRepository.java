package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<ProductJpaEntity> searchByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE p.category = :category
              AND (
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    Page<ProductJpaEntity> searchByCategoryAndKeyword(
            @Param("category") ProductCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
