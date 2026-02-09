package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.GetPopularProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인기 상품 목록 조회 응답")
public class GetPopularProductsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "인기 상품 목록", implementation = GetPopularProductsDto.Response.class)
    public GetPopularProductsDto.Response data;
}
