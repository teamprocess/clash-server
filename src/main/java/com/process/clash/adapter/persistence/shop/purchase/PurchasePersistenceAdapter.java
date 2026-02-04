package com.process.clash.adapter.persistence.shop.purchase;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.shop.purchase.port.out.PurchaseRepositoryPort;
import com.process.clash.domain.shop.purchase.entity.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchasePersistenceAdapter implements PurchaseRepositoryPort {

    private final PurchaseJpaRepository purchaseJpaRepository;
    private final PurchaseJpaMapper purchaseJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Purchase save(Purchase purchase) {
        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(purchase.userId());
        ProductJpaEntity productJpaEntity = productJpaRepository.getReferenceById(purchase.productId());
        PurchaseJpaEntity savedEntity = purchaseJpaRepository.save(
                purchaseJpaMapper.toJpaEntity(purchase, userJpaEntity, productJpaEntity)
        );
        return purchaseJpaMapper.toDomain(savedEntity);
    }
}
