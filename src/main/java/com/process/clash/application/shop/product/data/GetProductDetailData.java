package com.process.clash.application.shop.product.data;

import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;

public class GetProductDetailData {

    public record Command(
            Long productId
    ) {}

    public record Result(
            ProductVo product
    ) {
        public static Result from(Product domain, String seasonTitle) {
            return new Result(ProductVo.from(domain, seasonTitle));
        }
    }
}
