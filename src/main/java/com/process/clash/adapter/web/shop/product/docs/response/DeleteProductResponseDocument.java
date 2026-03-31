package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.DeleteProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 삭제 응답")
public class DeleteProductResponseDocument extends SuccessResponseDocument {

    @Schema(description = "삭제된 상품 정보", implementation = DeleteProductDto.Response.class)
    public DeleteProductDto.Response data;
}
