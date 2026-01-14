package com.process.clash.application.shop.product.port.out;

import com.process.clash.domain.shop.product.entity.Product;

public interface ProductRepositoryPort {
    Product save(Product product);
}
