package com.process.clash.adapter.web.shop.product.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.shop.product.docs.controller.ProductAdminControllerDocument;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.shop.product.dto.CreateProductDto;
import com.process.clash.adapter.web.shop.product.dto.DeleteProductDto;
import com.process.clash.adapter.web.shop.product.dto.UpdateProductDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.CreateProductData;
import com.process.clash.application.shop.product.data.DeleteProductData;
import com.process.clash.application.shop.product.data.UpdateProductData;
import com.process.clash.application.shop.product.port.in.CreateProductUseCase;
import com.process.clash.application.shop.product.port.in.DeleteProductUseCase;
import com.process.clash.application.shop.product.port.in.UpdateProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/shop/products")
@RequiredArgsConstructor
public class ProductAdminController implements ProductAdminControllerDocument {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    // 상품 생성
    @PostMapping
    public ApiResponse<CreateProductDto.Response> createProduct(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateProductDto.Request request
    ) {
        CreateProductData.Command command = request.toCommand(actor);
        CreateProductData.Result result = createProductUseCase.execute(command);
        CreateProductDto.Response response = CreateProductDto.Response.from(result);
        return ApiResponse.created(response, "상품 생성에 성공했습니다.");
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ApiResponse<UpdateProductDto.Response> updateProduct(
            @AuthenticatedActor Actor actor,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductDto.Request request
    ) {
        UpdateProductData.Command command = request.toCommand(productId, actor);
        UpdateProductData.Result result = updateProductUseCase.execute(command);
        UpdateProductDto.Response response = UpdateProductDto.Response.from(result);
        return ApiResponse.success(response, "상품 수정에 성공했습니다.");
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ApiResponse<DeleteProductDto.Response> deleteProduct(
            @AuthenticatedActor Actor actor,
            @PathVariable Long productId
    ) {
        DeleteProductData.Result result = deleteProductUseCase.execute(
                new DeleteProductData.Command(actor, productId)
        );
        DeleteProductDto.Response response = DeleteProductDto.Response.from(result);
        return ApiResponse.success(response, "상품 삭제에 성공했습니다.");
    }
}
