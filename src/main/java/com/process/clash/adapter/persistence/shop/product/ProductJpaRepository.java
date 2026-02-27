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
    Page<ProductJpaEntity> findByDiscountGreaterThan(Integer discount, Pageable pageable);

    @EntityGraph(attributePaths = "season")
    Page<ProductJpaEntity> findByCategoryAndDiscountGreaterThan(ProductCategory category, Integer discount, Pageable pageable);

    @EntityGraph(attributePaths = "season")
    List<ProductJpaEntity> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE p.title ILIKE CONCAT('%', :keyword, '%')
               OR p.description ILIKE CONCAT('%', :keyword, '%')
            """)
    Page<ProductJpaEntity> searchByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE p.discount > 0
              AND (
                    p.title ILIKE CONCAT('%', :keyword, '%')
                 OR p.description ILIKE CONCAT('%', :keyword, '%')
              )
            """)
    Page<ProductJpaEntity> searchDiscountedByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE p.category = :category
              AND (
                    p.title ILIKE CONCAT('%', :keyword, '%')
                 OR p.description ILIKE CONCAT('%', :keyword, '%')
              )
            """)
    Page<ProductJpaEntity> searchByCategoryAndKeyword(
            @Param("category") ProductCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "season")
    @Query("""
            SELECT p
            FROM ProductJpaEntity p
            WHERE p.category = :category
              AND p.discount > 0
              AND (
                    p.title ILIKE CONCAT('%', :keyword, '%')
                 OR p.description ILIKE CONCAT('%', :keyword, '%')
              )
            """)
    Page<ProductJpaEntity> searchDiscountedByCategoryAndKeyword(
            @Param("category") ProductCategory category,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
