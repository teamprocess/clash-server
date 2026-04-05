package com.process.clash.application.shop.product.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.shop.product.data.DeleteProductData;
import com.process.clash.application.shop.product.exception.exception.conflict.ProductInUseException;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.in.DeleteProductUseCase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public DeleteProductData.Result execute(DeleteProductData.Command command) {
        checkAdminPolicy.check(command.actor());

        productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        try {
            productRepositoryPort.deleteById(command.productId());
        } catch (DataIntegrityViolationException exception) {
            throw new ProductInUseException(exception);
        }

        return DeleteProductData.Result.from(command.productId());
    }
}
