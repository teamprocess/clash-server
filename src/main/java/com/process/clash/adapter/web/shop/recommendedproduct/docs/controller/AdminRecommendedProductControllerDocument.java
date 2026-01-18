package com.process.clash.adapter.web.shop.recommendedproduct.docs.controller;

import com.process.clash.adapter.web.shop.recommendedproduct.docs.request.CreateRecommendedProductRequestDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.request.UpdateRecommendedProductOrderRequestDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.request.UpdateRecommendedProductRequestDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.response.CreateRecommendedProductResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.response.DeleteRecommendedProductResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.response.UpdateRecommendedProductOrderResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.docs.response.UpdateRecommendedProductResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.CreateRecommendedProductDto;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductDto;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductOrderDto;
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
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "추천 상품 관리 API", description = "추천 상품 등록/수정/삭제")
public interface AdminRecommendedProductControllerDocument {

    @Operation(summary = "추천 상품 등록", description = "추천 상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateRecommendedProductResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "추천 상품 등록을 성공했습니다.",
                                      "data": {
                                        "id": 1,
                                        "productId": 100,
                                        "seasonName": "2025 봄 시즌",
                                        "displayOrder": 1,
                                        "startDate": "2025-03-01",
                                        "endDate": "2025-05-31",
                                        "isActive": true
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateRecommendedProductDto.Response> createRecommendedProduct(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "추천 상품 등록 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateRecommendedProductRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "productId": 100,
                                      "displayOrder": 1,
                                      "startDate": "2025-03-01",
                                      "endDate": "2025-05-31"
                                    }
                                    """)
                    ))
            CreateRecommendedProductDto.Request request
    );

    @Operation(summary = "추천 상품 수정", description = "추천 상품 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateRecommendedProductResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "추천 상품 수정을 성공했습니다.",
                                      "data": {
                                        "id": 1,
                                        "productId": 100,
                                        "seasonName": "2025 봄 시즌",
                                        "displayOrder": 2,
                                        "startDate": "2025-03-05",
                                        "endDate": "2025-05-31",
                                        "isActive": true
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateRecommendedProductDto.Response> updateRecommendedProduct(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "추천 상품 ID", example = "1") @PathVariable Long recommendedProductId,
            @RequestBody(description = "추천 상품 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateRecommendedProductRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "displayOrder": 2,
                                      "startDate": "2025-03-05",
                                      "endDate": "2025-05-31",
                                      "isActive": true
                                    }
                                    """)
                    ))
            UpdateRecommendedProductDto.Request request
    );

    @Operation(summary = "추천 상품 삭제", description = "추천 상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteRecommendedProductResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "추천 상품 삭제를 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteRecommendedProduct(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "추천 상품 ID", example = "1") @PathVariable Long recommendedProductId
    );

    @Operation(summary = "추천 상품 순서 변경", description = "추천 상품 노출 순서를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateRecommendedProductOrderResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "추천 상품 순서를 변경했습니다.",
                                      "data": {
                                        "id": 1,
                                        "displayOrder": 3
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateRecommendedProductOrderDto.Response> updateRecommendedProductOrder(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "추천 상품 ID", example = "1") @PathVariable Long recommendedProductId,
            @RequestBody(description = "추천 상품 순서 변경 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateRecommendedProductOrderRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "displayOrder": 3
                                    }
                                    """)
                    ))
            UpdateRecommendedProductOrderDto.Request request
    );
}
