package com.process.clash.application.ranking.service;

import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userrankhistory.port.out.UserRankHistoryRepositoryPort;
import com.process.clash.domain.shop.season.entity.Season;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingTierUpdateService {

    private static final int AURA_EXP_THRESHOLD = 100_000;
    private static final int MASTER_EXP_THRESHOLD = 75_000;

    private final UserRepositoryPort userRepositoryPort;
    private final UserRankHistoryRepositoryPort userRankHistoryRepositoryPort;
    private final SeasonRepositoryPort seasonRepositoryPort;

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

        Optional<Season> currentSeason = seasonRepositoryPort.findCurrentSeason();
        if (currentSeason.isEmpty()) {
            log.warn("활성 시즌이 없어 RankHistory를 저장하지 않습니다.");
        }

        LocalDate today = LocalDate.now();
        List<UserRankHistory> histories = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int rank = i + 1;
            RankTier newRankTier = computeRankTier(user.totalExp(), rank);

            if (user.currentRankTier() != newRankTier) {
                userRepositoryPort.save(user.withRankTier(newRankTier));
                log.debug("RankTier 변경. userId={}, rank={}, exp={}, {} -> {}",
                        user.id(), rank, user.totalExp(), user.currentRankTier(), newRankTier);
            }

            currentSeason.ifPresent(season -> histories.add(new UserRankHistory(
                    null, null, today, rank,
                    ExpTier.fromExp(user.totalExp()),
                    newRankTier,
                    user.id(),
                    season.id()
            )));
        }

        if (!histories.isEmpty()) {
            userRankHistoryRepositoryPort.saveAll(histories);
            log.info("RankHistory 저장 완료. 저장 수={}", histories.size());
        }

        log.info("RankTier 업데이트 완료");
    }

    private RankTier computeRankTier(int exp, int rank) {
        if (rank == 1 && exp >= AURA_EXP_THRESHOLD) return RankTier.AURA;
        if (rank <= 3 && exp >= MASTER_EXP_THRESHOLD) return RankTier.MASTER;
        return RankTier.NONE;
    }
}