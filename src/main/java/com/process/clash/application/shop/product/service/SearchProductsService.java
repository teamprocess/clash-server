package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.shop.product.data.SearchProductsData;
import com.process.clash.application.shop.product.port.in.SearchProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchProductsService implements SearchProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;

    @Override
    public SearchProductsData.Result execute(SearchProductsData.Command command) {
        ProductRepositoryPort.PageResult pageResult = productRepositoryPort.searchByKeywordByPage(
                command.page(),
                command.size(),
                command.sort(),
                command.category(),
                command.keyword()
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

        Pagination pagination = Pagination.from(
                command.page(),
                command.size(),
                totalCount
        );

        return new SearchProductsData.Result(productVos, pagination);
    }
}
