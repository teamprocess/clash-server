package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    List<ProductJpaEntity> findTop10ByOrderByPopularityDesc();

    Page<ProductJpaEntity> findByCategory(ProductCategory category, Pageable pageable);
}
