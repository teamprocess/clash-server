package com.process.clash.adapter.persistence.user.usergoodshistory;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserGoodsHistoryPersistenceAdapter implements UserGoodsHistoryRepositoryPort {

    private final UserGoodsHistoryJpaRepository userGoodsHistoryJpaRepository;
    private final UserGoodsJpaMapper userGoodsJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UserGoodsHistory save(UserGoodsHistory userGoodsHistory) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userGoodsHistory.userId());
        ProductJpaEntity productJpaEntity = productJpaRepository.getReferenceById(userGoodsHistory.productId());
        UserGoodsHistoryJpaEntity savedEntity = userGoodsHistoryJpaRepository.save(userGoodsJpaMapper.toJpaEntity(userGoodsHistory, userJpaEntity, productJpaEntity));
        return userGoodsJpaMapper.toDomain(savedEntity);
    }
}
