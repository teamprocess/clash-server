package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.adapter.persistence.shop.season.SeasonJpaMapper;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductJpaMapper {

    private final SeasonJpaMapper seasonJpaMapper;

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
                product.season() != null ? seasonJpaMapper.toJpaEntity(product.season()) : null,
                product.isSeasonal()
        );
    }

    public Product toDomain(ProductJpaEntity productJpaEntity) {
        Season season = productJpaEntity.getSeason() != null
                ? seasonJpaMapper.toDomain(productJpaEntity.getSeason())
                : null;

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
                season,
                productJpaEntity.getIsSeasonal()
        );
    }
}
