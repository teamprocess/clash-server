package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.shop.product.data.UpdateProductData;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.in.UpdateProductUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.season.exception.exception.notfound.SeasonNotFoundException;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateProductData.Result execute(UpdateProductData.Command command) {
        checkAdminPolicy.check(command.actor());

        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        Season season = null;
        if (command.seasonId() != null) {
            season = seasonRepositoryPort.findById(command.seasonId())
                    .orElseThrow(SeasonNotFoundException::new);
        }

        Product updatedProduct = product.update(
                command.title(),
                command.category(),
                command.image(),
                command.price(),
                command.discount(),
                command.description(),
                season
        );

        Product savedProduct = productRepositoryPort.save(updatedProduct);
        return UpdateProductData.Result.from(savedProduct);
    }
}
