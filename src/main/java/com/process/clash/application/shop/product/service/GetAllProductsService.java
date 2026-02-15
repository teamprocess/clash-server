package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.application.shop.product.port.in.GetAllProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetAllProductsService implements GetAllProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;

    @Override
    public GetAllProductsData.Result execute(GetAllProductsData.Command command) {
        ProductRepositoryPort.PageResult pageResult = productRepositoryPort.findAllByPage(
                command.page(),
                command.size(),
                command.sort(),
                command.category()
        );

        List<Product> products = pageResult.products();
        Long totalCount = pageResult.totalCount();
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

        Pagination pagination =
                Pagination.from(
                        command.page(),
                        command.size(),
                        totalCount
                );

        return new GetAllProductsData.Result(productVos, pagination);
    }
}
