package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.SearchProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;

public class SearchProductsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "검색된 상품 목록", implementation = SearchProductsDto.Response.class)
    public SearchProductsDto.Response data;
}
