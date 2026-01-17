package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.CreateProductData;
import com.process.clash.application.shop.product.port.in.CreateProductUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public CreateProductData.Result execute(CreateProductData.Command command) {
        checkAdminPolicy.check(command.actor());

        Season season = null;
        if (command.season() != null) {
            Long seasonId = Long.parseLong(command.season());
            season = seasonRepositoryPort.findById(seasonId).orElse(null);
        }

        Product product = Product.create(
                command.title(),
                command.category(),
                command.image(),
                command.type(),
                command.price(),
                command.discount(),
                command.description(),
                season
        );

        Product savedProduct = productRepositoryPort.save(product);

        return CreateProductData.Result.from(savedProduct.id());
    }
}
