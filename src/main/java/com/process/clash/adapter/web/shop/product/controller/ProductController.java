package com.process.clash.adapter.web.shop.product.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.shop.product.dto.GetAllProductsDto;
import com.process.clash.adapter.web.shop.product.dto.GetPopularProductsDto;
import com.process.clash.adapter.web.shop.product.dto.GetProductDetailDto;
import com.process.clash.adapter.web.shop.product.dto.GetRecommendedProductsDto;
import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.application.shop.product.data.GetPopularProductsData;
import com.process.clash.application.shop.product.data.GetProductDetailData;
import com.process.clash.application.shop.product.port.in.GetAllProductsUseCase;
import com.process.clash.application.shop.product.port.in.GetPopularProductsUseCase;
import com.process.clash.application.shop.product.port.in.GetProductDetailUseCase;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.in.GetRecommendedProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/products")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductDetailUseCase getProductDetailUseCase;
    private final GetPopularProductsUseCase getPopularProductsUseCase;
    private final GetRecommendedProductsUseCase getRecommendedProductsUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;

    // 전체 상품 목록 조회
    @GetMapping
    public ApiResponse<GetAllProductsDto.Response> getAllProducts(
            @ModelAttribute GetAllProductsDto.Request request
    ) {
        GetAllProductsData.Command command = request.toCommand();
        GetAllProductsData.Result result = getAllProductsUseCase.execute(command);
        GetAllProductsDto.Response response = GetAllProductsDto.Response.from(result);
        return ApiResponse.success(response, "전체 상품 목록 조회를 성공했습니다.");
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ApiResponse<GetProductDetailDto.Response> getProductDetail(
            @PathVariable Long productId
    ) {
        GetProductDetailData.Command command = new GetProductDetailData.Command(productId);
        GetProductDetailData.Result result = getProductDetailUseCase.execute(command);
        GetProductDetailDto.Response response = GetProductDetailDto.Response.from(result);
        return ApiResponse.success(response, "상품 상세 정보 조회를 성공했습니다.");
    }

    // 인기 상품 목록 조회 (10개)
    @GetMapping("/popular")
    public ApiResponse<GetPopularProductsDto.Response> getPopularProducts() {
        GetPopularProductsData.Result result = getPopularProductsUseCase.execute();
        GetPopularProductsDto.Response response = GetPopularProductsDto.Response.from(result);
        return ApiResponse.success(response, "인기 상품 목록 조회를 성공했습니다");
    }

    // 추천 상품 목록 조회 (10개)
    @GetMapping("/recommended")
    public ApiResponse<GetRecommendedProductsDto.Response> getRecommendedProducts() {
        GetRecommendedProductsData.Result result = getRecommendedProductsUseCase.execute();
        GetRecommendedProductsDto.Response response = GetRecommendedProductsDto.Response.from(result);
        return ApiResponse.success(response, "추천 상품 목록 조회를 성공했습니다.");
    }
}
