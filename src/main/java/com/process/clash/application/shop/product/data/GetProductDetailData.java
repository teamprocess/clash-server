package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;

public class GetProductDetailData {

    public record Command(
            Actor actor,
            Long productId
    ) {}

    public record Result(
            ProductVo product
    ) {
        public static Result from(Product domain, boolean isBought) {
            return new Result(ProductVo.from(domain, isBought));
        }
    }
}
