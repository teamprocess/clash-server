package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.application.shop.product.port.in.GetAllProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllProductsService implements GetAllProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;

    @Override
    public GetAllProductsData.Result execute(GetAllProductsData.Command command) {

        ProductRepositoryPort.PageResult pageResult = productRepositoryPort.findAll(
                command.page(),
                command.size(),
                command.sort(),
                command.category()
        );

        List<Product> products = pageResult.products();
        Long totalCount = pageResult.totalCount();

        List<ProductVo> productVos = products.stream()
                .map(product -> {
                    String seasonTitle = null;
                    if (product.seasonId() != null) {
                        seasonTitle = seasonRepositoryPort.findById(product.seasonId())
                                .map(Season::title)
                                .orElse(null);
                    }
                    return ProductVo.from(product, seasonTitle);
                })
                .toList();

        GetAllProductsData.PaginationInfo pagination =
                GetAllProductsData.PaginationInfo.from(
                        command.page(),
                        command.size(),
                        totalCount
                );

        return new GetAllProductsData.Result(productVos, pagination);
    }
}
