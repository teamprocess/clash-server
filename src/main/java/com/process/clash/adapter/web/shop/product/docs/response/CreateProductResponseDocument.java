package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.CreateProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 생성 응답")
public class CreateProductResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 상품 정보", implementation = CreateProductDto.Response.class)
    public CreateProductDto.Response data;
}
