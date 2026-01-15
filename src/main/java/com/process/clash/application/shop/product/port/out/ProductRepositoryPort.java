package com.process.clash.application.shop.product.port.out;

import com.process.clash.domain.shop.product.entity.Product;

import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long productId);
}
