package com.process.clash.adapter.web.shop.purchase.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.shop.purchase.docs.controller.PurchaseControllerDocument;
import com.process.clash.adapter.web.shop.purchase.dto.CreatePurchaseDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.purchase.data.CreatePurchaseData;
import com.process.clash.application.shop.purchase.port.in.CreatePurchaseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/purchases")
@RequiredArgsConstructor
public class PurchaseController implements PurchaseControllerDocument {

    private final CreatePurchaseUseCase createPurchaseUseCase;

    @PostMapping
    public ApiResponse<CreatePurchaseDto.Response> createPurchase(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreatePurchaseDto.Request request
    ) {
        CreatePurchaseData.Command command = request.toCommand(actor);
        CreatePurchaseData.Result result = createPurchaseUseCase.execute(command);
        CreatePurchaseDto.Response response = CreatePurchaseDto.Response.from(result);
        return ApiResponse.created(response, "상품 구매를 성공했습니다.");
    }
}
