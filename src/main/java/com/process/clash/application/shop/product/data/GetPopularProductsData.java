package com.process.clash.application.shop.product.data;

import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class GetPopularProductsData {

    public record Result(
            List<ProductVo> products
    ) {
        public static Result from(List<Product> domains, List<String> seasonTitles) {
            List<ProductVo> products = new ArrayList<>();
            for (int i = 0; i < domains.size(); i++) {
                String seasonTitle = seasonTitles.get(i);
                products.add(ProductVo.from(domains.get(i), seasonTitle));
            }
            return new Result(products);
        }
    }
}
