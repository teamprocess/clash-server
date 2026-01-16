package com.process.clash.application.shop.recommendedproduct.service;

import com.process.clash.application.shop.recommendedproduct.data.DeleteRecommendedProductData;
import com.process.clash.application.shop.recommendedproduct.exception.exception.notfound.RecommendedProductNotFoundException;
import com.process.clash.application.shop.recommendedproduct.port.in.DeleteRecommendedProductUseCase;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteRecommendedProductService implements DeleteRecommendedProductUseCase {

    private final RecommendedProductRepositoryPort recommendedProductRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public void execute(DeleteRecommendedProductData.Command command) {

        checkAdminPolicy.check(command.actor());

        recommendedProductRepositoryPort.findById(command.recommendedProductId())
                .orElseThrow(RecommendedProductNotFoundException::new);

        recommendedProductRepositoryPort.deleteById(command.recommendedProductId());
    }
}
