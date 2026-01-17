package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductOrderData;
import com.process.clash.application.shop.recommendedproduct.exception.exception.notfound.RecommendedProductNotFoundException;
import com.process.clash.application.shop.recommendedproduct.port.in.UpdateRecommendedProductOrderUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateRecommendedProductOrderService implements UpdateRecommendedProductOrderUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateRecommendedProductOrderData.Result execute(UpdateRecommendedProductOrderData.Command command) {

        checkAdminPolicy.check(command.actor());

        RecommendedProduct recommendedProduct = recommendedProductRepositoryPort.findById(command.recommendedProductId())
                .orElseThrow(RecommendedProductNotFoundException::new);

        RecommendedProduct updatedRecommendedProduct = recommendedProduct.updateOrder(command.displayOrder());

        RecommendedProduct savedRecommendedProduct = recommendedProductRepositoryPort.save(updatedRecommendedProduct);

        return UpdateRecommendedProductOrderData.Result.from(savedRecommendedProduct);
    }
}
