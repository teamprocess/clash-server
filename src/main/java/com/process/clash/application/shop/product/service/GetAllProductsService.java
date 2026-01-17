package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.application.shop.product.port.in.GetAllProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllProductsService implements GetAllProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

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

        List<ProductVo> productVos = products.stream()
                .map(ProductVo::from)
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
