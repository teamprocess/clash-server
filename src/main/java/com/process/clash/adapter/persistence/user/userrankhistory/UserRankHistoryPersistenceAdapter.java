package com.process.clash.adapter.persistence.user.userrankhistory;

import com.process.clash.adapter.persistence.shop.season.SeasonJpaEntity;
import com.process.clash.adapter.persistence.shop.season.SeasonJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userrankhistory.port.out.UserRankHistoryRepositoryPort;
import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRankHistoryPersistenceAdapter implements UserRankHistoryRepositoryPort {

    private final UserRankHistoryJpaRepository userRankHistoryJpaRepository;
    private final UserRankHistoryJpaMapper userRankHistoryJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final SeasonJpaRepository seasonJpaRepository;

    @Override
    public UserRankHistory save(UserRankHistory userRankHistory) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userRankHistory.userId());
        SeasonJpaEntity seasonJpaEntity = seasonJpaRepository.getReferenceById(userRankHistory.seasonId());
        UserRankHistoryJpaEntity savedEntity = userRankHistoryJpaRepository.save(userRankHistoryJpaMapper.toJpaEntity(userRankHistory, userJpaEntity, seasonJpaEntity));
        return userRankHistoryJpaMapper.toDomain(savedEntity);
    }
}
