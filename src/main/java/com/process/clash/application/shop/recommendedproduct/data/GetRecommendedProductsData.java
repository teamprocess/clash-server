package com.process.clash.application.shop.recommendedproduct.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;

import java.util.List;
import java.util.Set;

public class GetRecommendedProductsData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            List<ProductVo> products
    ) {
        public static Result from(List<Product> domains, Set<Long> ownedProductIds) {
            List<ProductVo> products = domains.stream()
                    .map(product -> ProductVo.from(product, ownedProductIds.contains(product.id())))
                    .toList();
            return new Result(products);
        }
    }
}
