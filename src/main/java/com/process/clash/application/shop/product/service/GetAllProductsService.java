package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.pagination.Pagination;
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
    private final ProductVoConverter productVoConverter;

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
        List<ProductVo> productVos = productVoConverter.toProductVos(products, command.actor());

        Pagination pagination =
                Pagination.from(
                        command.page(),
                        command.size(),
                        totalCount
                );

        return new GetAllProductsData.Result(productVos, pagination);
    }
}
