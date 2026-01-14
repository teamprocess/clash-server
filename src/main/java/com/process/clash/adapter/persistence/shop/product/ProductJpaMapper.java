package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.domain.shop.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductJpaMapper {

    public ProductJpaEntity toJpaEntity(Product product) {
        return new ProductJpaEntity(
                product.id(),
                product.createdAt(),
                product.updatedAt(),
                product.title(),
                product.category(),
                product.image(),
                product.type(),
                product.price(),
                product.discount(),
                product.description(),
                product.popularity(),
                null,
                product.isSeasonal()
        );
    }

    public Product toDomain(ProductJpaEntity productJpaEntity) {
        return new Product(
                productJpaEntity.getId(),
                productJpaEntity.getCreatedAt(),
                productJpaEntity.getUpdatedAt(),
                productJpaEntity.getTitle(),
                productJpaEntity.getCategory(),
                productJpaEntity.getImage(),
                productJpaEntity.getType(),
                productJpaEntity.getPrice(),
                productJpaEntity.getDiscount(),
                productJpaEntity.getDescription(),
                productJpaEntity.getPopularity(),
                productJpaEntity.getSeason() != null ? productJpaEntity.getSeason().getId() : null,
                productJpaEntity.getIsSeasonal()
        );
    }
}
