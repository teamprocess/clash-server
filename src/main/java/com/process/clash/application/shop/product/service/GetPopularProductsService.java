package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.GetPopularProductsData;
import com.process.clash.application.shop.product.port.in.GetPopularProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPopularProductsService implements GetPopularProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;

    @Override
    public GetPopularProductsData.Result execute(GetPopularProductsData.Command command) {
        List<Product> products = productRepositoryPort.findTop10ByOrderByPopularityDesc();
        Set<Long> productIds = products.stream()
                .map(Product::id)
                .collect(java.util.stream.Collectors.toSet());
        Set<Long> ownedProductIds = userItemRepositoryPort.findOwnedProductIdsByUserIdAndProductIds(
                command.actor().id(),
                productIds
        );

        List<ProductVo> productVos = products.stream()
                .map(product -> ProductVo.from(product, ownedProductIds.contains(product.id())))
                .toList();

        return new GetPopularProductsData.Result(productVos);
    }
}
