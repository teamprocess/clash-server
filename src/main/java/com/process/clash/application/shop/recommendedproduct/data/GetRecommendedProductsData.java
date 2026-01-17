package com.process.clash.application.shop.recommendedproduct.data;

import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;

import java.util.List;

public class GetRecommendedProductsData {

    public record Result(
            List<ProductVo> products
    ) {
        public static Result from(List<Product> domains) {
            List<ProductVo> products = domains.stream()
                    .map(ProductVo::from)
                    .toList();
            return new Result(products);
        }
    }
}
