package com.process.clash.adapter.web.shop.recommendedproduct.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.CreateRecommendedProductDto;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductDto;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductOrderDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.data.DeleteRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductOrderData;
import com.process.clash.application.shop.recommendedproduct.port.in.CreateRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.in.DeleteRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.in.UpdateRecommendedProductOrderUseCase;
import com.process.clash.application.shop.recommendedproduct.port.in.UpdateRecommendedProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/shop/products/recommendations")
@RequiredArgsConstructor
public class AdminRecommendedProductController {

    private final CreateRecommendedProductUseCase createRecommendedProductUseCase;
    private final UpdateRecommendedProductUseCase updateRecommendedProductUseCase;
    private final DeleteRecommendedProductUseCase deleteRecommendedProductUseCase;
    private final UpdateRecommendedProductOrderUseCase updateRecommendedProductOrderUseCase;

    // 추천 상품 등록
    @PostMapping
    public ApiResponse<CreateRecommendedProductDto.Response> createRecommendedProduct(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateRecommendedProductDto.Request request
    ) {
        CreateRecommendedProductData.Command command = request.toCommand(actor);
        CreateRecommendedProductData.Result result = createRecommendedProductUseCase.execute(command);
        CreateRecommendedProductDto.Response response = CreateRecommendedProductDto.Response.from(result);
        return ApiResponse.created(response, "추천 상품 등록을 성공했습니다.");
    }

    // 추천 상품 수정
    @PutMapping("/{recommendedProductId}")
    public ApiResponse<UpdateRecommendedProductDto.Response> updateRecommendedProduct(
            @AuthenticatedActor Actor actor,
            @PathVariable Long recommendedProductId,
            @Valid @RequestBody UpdateRecommendedProductDto.Request request
    ) {
        UpdateRecommendedProductData.Command command = request.toCommand(recommendedProductId, actor);
        UpdateRecommendedProductData.Result result = updateRecommendedProductUseCase.execute(command);
        UpdateRecommendedProductDto.Response response = UpdateRecommendedProductDto.Response.from(result);
        return ApiResponse.success(response, "추천 상품 수정을 성공했습니다.");
    }

    // 추천 상품 삭제
    @DeleteMapping("/{recommendedProductId}")
    public ApiResponse<Void> deleteRecommendedProduct(
            @AuthenticatedActor Actor actor,
            @PathVariable Long recommendedProductId
    ) {
        DeleteRecommendedProductData.Command command = new DeleteRecommendedProductData.Command(actor, recommendedProductId);
        deleteRecommendedProductUseCase.execute(command);
        return ApiResponse.success("추천 상품 삭제를 성공했습니다.");
    }

    // 추천 상품 순서 변경
    @PatchMapping("/{recommendedProductId}/order")
    public ApiResponse<UpdateRecommendedProductOrderDto.Response> updateRecommendedProductOrder(
            @AuthenticatedActor Actor actor,
            @PathVariable Long recommendedProductId,
            @Valid @RequestBody UpdateRecommendedProductOrderDto.Request request
    ) {
        UpdateRecommendedProductOrderData.Command command = request.toCommand(recommendedProductId, actor);
        UpdateRecommendedProductOrderData.Result result = updateRecommendedProductOrderUseCase.execute(command);
        UpdateRecommendedProductOrderDto.Response response = UpdateRecommendedProductOrderDto.Response.from(result);
        return ApiResponse.success(response, "추천 상품 순서를 변경했습니다.");
    }
}
