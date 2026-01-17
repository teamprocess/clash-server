package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.GetProductDetailData;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.in.GetProductDetailUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductDetailService implements GetProductDetailUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;

    @Override
    public GetProductDetailData.Result execute(GetProductDetailData.Command command) {

        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        String seasonTitle = null;
        if (product.seasonId() != null) {
            seasonTitle = seasonRepositoryPort.findById(product.seasonId())
                    .map(Season::title)
                    .orElse(null);
        }

        return GetProductDetailData.Result.from(product, seasonTitle);
    }
}