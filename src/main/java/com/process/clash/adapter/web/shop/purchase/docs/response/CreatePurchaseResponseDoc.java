package com.process.clash.adapter.web.shop.purchase.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.shop.purchase.dto.CreatePurchaseDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 구매 응답")
public class CreatePurchaseResponseDoc extends SuccessResponseDoc {

    @Schema(description = "생성된 구매 정보", implementation = CreatePurchaseDto.Response.class)
    public CreatePurchaseDto.Response data;
}
