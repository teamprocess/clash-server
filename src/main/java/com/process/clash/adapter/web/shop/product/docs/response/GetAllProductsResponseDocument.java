package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.GetAllProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 상품 목록 조회 응답")
public class GetAllProductsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "상품 목록", implementation = GetAllProductsDto.Response.class)
    public GetAllProductsDto.Response data;
}
