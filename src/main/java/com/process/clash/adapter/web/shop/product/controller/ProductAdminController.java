package com.process.clash.adapter.web.shop.product.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.shop.product.dto.CreateProductDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.CreateProductData;
import com.process.clash.application.shop.product.port.in.CreateProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/shop/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final CreateProductUseCase createProductUseCase;

    // 상품 생성
    @PostMapping
    public ApiResponse<CreateProductDto.Response> createProduct(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateProductDto.Request request
    ) {
        CreateProductData.Command command = CreateProductData.Command.from(request, actor);
        CreateProductData.Result result = createProductUseCase.execute(command);
        CreateProductDto.Response response = CreateProductDto.Response.from(result.productId());
        return ApiResponse.created(response, "상품 생성에 성공했습니다.");
    }
}
