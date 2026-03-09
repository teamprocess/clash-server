package com.process.clash.application.ranking.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingTierUpdateService {

    private static final int AURA_EXP_THRESHOLD = 100_000;
    private static final int MASTER_EXP_THRESHOLD = 75_000;

    private final UserRepositoryPort userRepositoryPort;

    /**
     * 랭킹 기반 티어(Master, Aura) 업데이트
     * - Aura : 1위 && exp >= 100,000
     * - Master: 2~3위 && exp >= 75,000  (상위 3등 2명)
     * - 그 외 : NONE
     */
    @Transactional
    public void updateRankTiers() {
        List<User> users = userRepositoryPort.findAllOrderByTotalExpDesc();

        if (users.isEmpty()) {
            log.info("RankTier 업데이트 대상 유저 없음");
            return;
        }

        log.info("RankTier 업데이트 시작. 대상 유저 수={}", users.size());

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int rank = i + 1;
            RankTier newRankTier = computeRankTier(user.totalExp(), rank);

            if (user.currentRankTier() != newRankTier) {
                userRepositoryPort.save(user.withRankTier(newRankTier));
                log.debug("RankTier 변경. userId={}, rank={}, exp={}, {} -> {}",
                        user.id(), rank, user.totalExp(), user.currentRankTier(), newRankTier);
            }
        }

        log.info("RankTier 업데이트 완료");
    }

    private RankTier computeRankTier(int exp, int rank) {
        if (rank == 1 && exp >= AURA_EXP_THRESHOLD) return RankTier.AURA;
        if (rank <= 3 && exp >= MASTER_EXP_THRESHOLD) return RankTier.MASTER;
        return RankTier.NONE;
    }
}