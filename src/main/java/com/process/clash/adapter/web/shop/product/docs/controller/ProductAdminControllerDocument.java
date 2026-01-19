package com.process.clash.adapter.web.shop.product.docs.controller;

import com.process.clash.adapter.web.shop.product.docs.request.CreateProductRequestDoc;
import com.process.clash.adapter.web.shop.product.docs.response.CreateProductResponseDoc;
import com.process.clash.adapter.web.shop.product.dto.CreateProductDto;
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

@Tag(name = "상품 관리 API", description = "상품 생성")
public interface ProductAdminControllerDocument {

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateProductResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "상품 생성에 성공했습니다.",
                                      "data": {
                                        "productId": 100
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateProductDto.Response> createProduct(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "상품 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateProductRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "프로세스 삼겹살 헌터",
                                      "category": "NAMEPLATE",
                                      "image": "https://cdn.example.com/products/100.png",
                                      "type": "COOKIE",
                                      "price": 12000,
                                      "discount": 10,
                                      "description": "프로세스 삼겹살 헌터",
                                      "seasonId": 1
                                    }
                                    """)
                    ))
            CreateProductDto.Request request
    );
}
