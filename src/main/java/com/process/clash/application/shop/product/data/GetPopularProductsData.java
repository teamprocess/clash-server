package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.vo.ProductVo;

import java.util.List;

public class GetPopularProductsData {

    public record Command(
            Actor actor
    ) {}

    public record Result(
            List<ProductVo> products
    ) {}
}
