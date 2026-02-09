package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.GetProductDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 상세 조회 응답")
public class GetProductDetailResponseDocument extends SuccessResponseDocument {

    @Schema(description = "상품 상세", implementation = GetProductDetailDto.Response.class)
    public GetProductDetailDto.Response data;
}
