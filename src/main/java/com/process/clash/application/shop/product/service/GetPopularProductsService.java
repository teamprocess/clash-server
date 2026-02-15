package com.process.clash.application.shop.product.service;

import com.process.clash.application.shop.product.data.GetPopularProductsData;
import com.process.clash.application.shop.product.port.in.GetPopularProductsUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPopularProductsService implements GetPopularProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVoConverter productVoConverter;

    @Override
    public GetPopularProductsData.Result execute(GetPopularProductsData.Command command) {
        List<Product> products = productRepositoryPort.findTop10ByOrderByPopularityDesc();
        List<ProductVo> productVos = productVoConverter.toProductVos(products, command.actor());

        return new GetPopularProductsData.Result(productVos);
    }
}
