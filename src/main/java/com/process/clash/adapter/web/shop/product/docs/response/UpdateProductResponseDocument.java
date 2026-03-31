package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.UpdateProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 수정 응답")
public class UpdateProductResponseDocument extends SuccessResponseDocument {

    @Schema(description = "수정된 상품 정보", implementation = UpdateProductDto.Response.class)
    public UpdateProductDto.Response data;
}
