package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.shop.product.dto.GetAllProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 상품 목록 조회 응답")
public class GetAllProductsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "상품 목록", implementation = GetAllProductsDto.Response.class)
    public GetAllProductsDto.Response data;
}
