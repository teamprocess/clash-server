package com.process.clash.adapter.persistence.user.usercookiehistory;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.shop.product.ProductJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.usercookiehistory.port.out.UserCookieHistoryRepositoryPort;
import com.process.clash.domain.user.usercookiehistory.entity.UserCookieHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCookieHistoryPersistenceAdapter implements UserCookieHistoryRepositoryPort {

    private final UserCookieHistoryJpaRepository userCookieHistoryJpaRepository;
    private final UserCookieJpaMapper userCookieJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    @Override
    public UserCookieHistory save(UserCookieHistory userCookieHistory) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userCookieHistory.userId());
        ProductJpaEntity productJpaEntity = productJpaRepository.getReferenceById(userCookieHistory.productId());
        UserCookieHistoryJpaEntity savedEntity = userCookieHistoryJpaRepository.save(userCookieJpaMapper.toJpaEntity(userCookieHistory, userJpaEntity, productJpaEntity));
        return userCookieJpaMapper.toDomain(savedEntity);
    }
}
