package com.process.clash.adapter.web.shop.product.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.shop.product.dto.GetPopularProductsDto;
import com.process.clash.adapter.web.shop.product.dto.GetProductDetailDto;
import com.process.clash.application.shop.product.data.GetPopularProductsData;
import com.process.clash.application.shop.product.data.GetProductDetailData;
import com.process.clash.application.shop.product.port.in.GetPopularProductsUseCase;
import com.process.clash.application.shop.product.port.in.GetProductDetailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/products")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductDetailUseCase getProductDetailUseCase;
    private final GetPopularProductsUseCase getPopularProductsUseCase;

    @GetMapping("/{productId}")
    public ApiResponse<GetProductDetailDto.Response> getProductDetail(
            @PathVariable Long productId
    ) {
        GetProductDetailData.Command command = new GetProductDetailData.Command(productId);
        GetProductDetailData.Result result = getProductDetailUseCase.execute(command);
        GetProductDetailDto.Response response = GetProductDetailDto.Response.from(result);
        return ApiResponse.success(response, "상품 상세 정보 조회에 성공했습니다.");
    }

    @GetMapping("/popular")
    public ApiResponse<GetPopularProductsDto.Response> getPopularProducts() {
        GetPopularProductsData.Result result = getPopularProductsUseCase.execute();
        GetPopularProductsDto.Response response = GetPopularProductsDto.Response.from(result);
        return ApiResponse.success(response, "인기 상품 목록 조회에 성공했습니다");
    }
}
