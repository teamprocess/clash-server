package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductVoConverter {

    private final UserItemRepositoryPort userItemRepositoryPort;

    public List<ProductVo> toProductVos(List<Product> products, Actor actor) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }

        if (actor == null || actor.id() == null) {
            return products.stream()
                    .map(ProductVo::from)
                    .toList();
        }

        Set<Long> productIds = products.stream()
                .map(Product::id)
                .collect(java.util.stream.Collectors.toSet());
        Set<Long> ownedProductIds = userItemRepositoryPort.findOwnedProductIdsByUserIdAndProductIds(
                actor.id(),
                productIds
        );

        return products.stream()
                .map(product -> ProductVo.from(product, ownedProductIds.contains(product.id())))
                .toList();
    }
}
