package com.process.clash.adapter.web.shop.purchase.docs.controller;

import com.process.clash.adapter.web.shop.purchase.docs.request.CreatePurchaseRequestDocument;
import com.process.clash.adapter.web.shop.purchase.docs.response.CreatePurchaseResponseDocument;
import com.process.clash.adapter.web.shop.purchase.dto.CreatePurchaseDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "상품 구매 API", description = "상품 구매")
public interface PurchaseControllerDocument {

    @Operation(summary = "상품 구매", description = "상품을 구매합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "구매 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreatePurchaseResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "상품 구매를 성공했습니다.",
                                      "data": {
                                        "purchaseId": 1
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreatePurchaseDto.Response> createPurchase(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "상품 구매 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreatePurchaseRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "productId": 100
                                    }
                                    """)
                    ))
            CreatePurchaseDto.Request request
    );
}
