package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.GetPopularProductsData;
import com.process.clash.application.shop.product.vo.ProductVo;

import java.util.List;

public class GetPopularProductsDto {

    public record Response(
            List<Product> products
    ) {
        public static Response from(GetPopularProductsData.Result result) {
            List<Product> products = result.products().stream()
                    .map(Product::from)
                    .toList();
            return new Response(products);
        }
    }

    public record Product(
            Long id,
            String title,
            String category,
            String image,
            String type,
            Long price,
            Integer discount,
            String description,
            Long popularity,
            String seasonName,
            Boolean isSeasonal,
            String createdAt
    ) {
        public static Product from(ProductVo product) {
            return new Product(
                    product.id(),
                    product.title(),
                    product.category().name(),
                    product.image(),
                    product.type().name(),
                    product.price(),
                    product.discount(),
                    product.description(),
                    product.popularity(),
                    product.seasonName(),
                    product.isSeasonal(),
                    product.createdAt().toString()
            );
        }
    }
}
