package com.process.clash.application.shop.product.data;

import com.process.clash.application.shop.product.vo.ProductVo;

import java.util.List;

public class GetPopularProductsData {

    public record Result(
            List<ProductVo> products
    ) {}
}
