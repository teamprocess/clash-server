package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.GetProductDetailData;

public interface GetProductDetailUseCase {
    GetProductDetailData.Result execute(GetProductDetailData.Command command);
}
