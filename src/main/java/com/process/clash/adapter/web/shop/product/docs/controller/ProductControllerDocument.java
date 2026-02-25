package com.process.clash.adapter.web.shop.product.docs.controller;

import com.process.clash.adapter.web.shop.product.docs.response.GetAllProductsResponseDocument;
import com.process.clash.adapter.web.shop.product.docs.response.GetPopularProductsResponseDocument;
import com.process.clash.adapter.web.shop.product.docs.response.GetProductDetailResponseDocument;
import com.process.clash.adapter.web.shop.product.docs.response.GetRecommendedProductsResponseDocument;
import com.process.clash.adapter.web.shop.product.docs.response.SearchProductsResponseDocument;
import com.process.clash.adapter.web.shop.product.dto.GetAllProductsDto;
import com.process.clash.adapter.web.shop.product.dto.GetPopularProductsDto;
import com.process.clash.adapter.web.shop.product.dto.GetProductDetailDto;
import com.process.clash.adapter.web.shop.product.dto.GetRecommendedProductsDto;
import com.process.clash.adapter.web.shop.product.dto.SearchProductsDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "상품 조회 API", description = "상품 조회")
public interface ProductControllerDocument {

    @Operation(summary = "전체 상품 목록 조회", description = "조건에 따라 상품 목록을 조회합니다.")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "20"),
            @Parameter(name = "sort", description = "정렬 기준", example = "POPULARITY"),
            @Parameter(name = "category", description = "카테고리 (INSIGNIA, NAMEPLATE, BANNER)", example = "INSIGNIA")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetAllProductsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전체 상품 목록 조회를 성공했습니다.",
                                      "data": {
                                        "products": [
                                          {
                                            "id": 100,
                                            "title": "1개월 휴가권",
                                            "category": "INSIGNIA",
                                            "image": "https://cdn.example.com/products/100.png",
                                            "price": 12000,
                                            "discount": 10,
                                            "description": "프로세스 1개월 휴가권",
                                            "popularity": 2,
                                            "seasonName": "2025 봄 시즌",
                                            "isSeasonal": true,
                                            "isBought": true,
                                            "createdAt": "2025-01-01T12:00:00+09:00"
                                          }
                                        ],
                                        "pagination": {
                                          "currentPage": 0,
                                          "totalPages": 3,
                                          "totalItems": 45,
                                          "pageSize": 20,
                                          "hasNext": true,
                                          "hasPrevious": false
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllProductsDto.Response> getAllProducts(
            @Parameter(hidden = true) Actor actor,
            @ParameterObject GetAllProductsDto.Request request
    );

    @Operation(summary = "상품 검색", description = "키워드와 조건으로 상품을 검색합니다.")
    @Parameters({
            @Parameter(name = "keyword", description = "검색어", example = "휴가권"),
            @Parameter(name = "page", description = "페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "20"),
            @Parameter(name = "sort", description = "정렬 기준", example = "POPULAR"),
            @Parameter(name = "category", description = "카테고리 (INSIGNIA, NAMEPLATE, BANNER)", example = "ALL")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(
                            schema = @Schema(implementation = SearchProductsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "상품 검색을 성공했습니다.",
                                      "data": {
                                        "products": [
                                          {
                                            "id": 100,
                                            "title": "1개월 휴가권",
                                            "category": "INSIGNIA",
                                            "image": "https://cdn.example.com/products/100.png",
                                            "price": 12000,
                                            "discount": 10,
                                            "description": "프로세스 1개월 휴가권",
                                            "popularity": 2,
                                            "seasonName": "2025 봄 시즌",
                                            "isSeasonal": true,
                                            "isBought": true,
                                            "createdAt": "2025-01-01T12:00:00+09:00"
                                          }
                                        ],
                                        "pagination": {
                                          "currentPage": 0,
                                          "totalPages": 1,
                                          "totalItems": 1,
                                          "pageSize": 20,
                                          "hasNext": false,
                                          "hasPrevious": false
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SearchProductsDto.Response> searchProducts(
            @Parameter(hidden = true) Actor actor,
            @ParameterObject SearchProductsDto.Request request
    );

    @Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetProductDetailResponseDocument.class),
                examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "상품 상세 정보 조회를 성공했습니다.",
                                      "data": {
                                        "product": {
                                            "id": 100,
                                            "title": "1개월 휴가권",
                                            "category": "INSIGNIA",
                                            "image": "https://cdn.example.com/products/100.png",
                                            "price": 12000,
                                            "discount": 10,
                                            "description": "프로세스 1개월 휴가권",
                                            "popularity": 2,
                                            "seasonName": "2025 봄 시즌",
                                            "isSeasonal": true,
                                            "isBought": true,
                                            "createdAt": "2025-01-01T12:00:00+09:00"
                                          }
                                      }
                                    }
                                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetProductDetailDto.Response> getProductDetail(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "상품 ID", example = "1") @PathVariable Long productId
    );

    @Operation(summary = "인기 상품 목록", description = "인기 상품 10개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetPopularProductsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "인기 상품 목록 조회를 성공했습니다",
                                      "data": {
                                        "products": [
                                          {
                                            "id": 100,
                                            "title": "1개월 휴가권",
                                            "category": "INSIGNIA",
                                            "image": "https://cdn.example.com/products/100.png",
                                            "price": 12000,
                                            "discount": 10,
                                            "description": "프로세스 1개월 휴가권",
                                            "popularity": 2,
                                            "seasonName": "2025 봄 시즌",
                                            "isSeasonal": true,
                                            "isBought": true,
                                            "createdAt": "2025-01-01T12:00:00+09:00"
                                          }
                                        ],
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetPopularProductsDto.Response> getPopularProducts(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "추천 상품 목록", description = "추천 상품 10개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetRecommendedProductsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "추천 상품 목록 조회를 성공했습니다.",
                                      "data": {
                                        "products": [
                                          {
                                            "id": 100,
                                            "title": "1개월 휴가권",
                                            "category": "INSIGNIA",
                                            "image": "https://cdn.example.com/products/100.png",
                                            "price": 12000,
                                            "discount": 10,
                                            "description": "프로세스 1개월 휴가권",
                                            "popularity": 2,
                                            "seasonName": "2025 봄 시즌",
                                            "isSeasonal": true,
                                            "isBought": true,
                                            "createdAt": "2025-01-01T12:00:00+09:00"
                                          }
                                        ],
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetRecommendedProductsDto.Response> getRecommendedProducts(
            @Parameter(hidden = true) Actor actor
    );
}
