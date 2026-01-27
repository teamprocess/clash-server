package com.process.clash.adapter.persistence.user.usergoodshistory;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserGoodsHistoryPersistenceAdapter implements UserGoodsHistoryRepositoryPort {

    private final UserGoodsHistoryJpaRepository userGoodsHistoryJpaRepository;
    private final UserGoodsHistoryJpaMapper userGoodsHistoryJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UserGoodsHistory save(UserGoodsHistory userGoodsHistory) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userGoodsHistory.userId());
        ProductJpaEntity productJpaEntity = userGoodsHistory.productId() != null ?
                productJpaRepository.getReferenceById(userGoodsHistory.productId()) : null;
        UserGoodsHistoryJpaEntity savedEntity = userGoodsHistoryJpaRepository.save(userGoodsHistoryJpaMapper.toJpaEntity(userGoodsHistory, userJpaEntity, productJpaEntity));
        return userGoodsHistoryJpaMapper.toDomain(savedEntity);
    }

    @Override
    public List<Long> findDistinctProductIdsByUserId(Long userId) {
        return userGoodsHistoryJpaRepository.findDistinctProductIdsByUserId(userId);
    }
}
