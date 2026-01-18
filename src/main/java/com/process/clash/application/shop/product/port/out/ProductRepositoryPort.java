package com.process.clash.application.shop.product.port.out;

import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long productId);
    List<Product> findAllByIdIn(List<Long> productIds);

    List<Product> findTop10ByOrderByPopularityDesc();

    record PageResult(List<Product> products, long totalCount) {}

    PageResult findAllByPage(
            Integer page,
            Integer size,
            ProductSortType sort,
            ProductCategory category
    );
}
