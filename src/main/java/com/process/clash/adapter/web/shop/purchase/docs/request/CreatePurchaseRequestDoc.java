package com.process.clash.adapter.web.shop.purchase.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 구매 요청")
public class CreatePurchaseRequestDoc {

    @Schema(description = "상품 ID", example = "100")
    public Long productId;
}
