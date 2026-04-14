package com.process.clash.application.ranking.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyRankingRewardService {

    private static final int[] COOKIE_REWARDS = {1000, 500, 300};
    private static final int REWARD_COUNT = 3;

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    /**
     * 전날 하루 동안 쌓은 EXP 기준 상위 3명에게 쿠키 보상을 지급한다.
     * 1위: 1000 쿠키, 2위: 500 쿠키, 3위: 300 쿠키
     */
    @Transactional
    public void rewardDailyRankingWinners() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        List<Long> topUserIds = userExpHistoryRepositoryPort.findTopUserIdsByDailyExp(yesterday, REWARD_COUNT);

        if (topUserIds.isEmpty()) {
            log.info("일별 랭킹 보상 지급 대상 없음. date={}", yesterday);
            return;
        }

        for (int i = 0; i < topUserIds.size(); i++) {
            Long userId = topUserIds.get(i);
            int cookies = COOKIE_REWARDS[i];

            User user = userRepositoryPort.findByIdForUpdate(userId).orElse(null);
            if (user == null) {
                log.warn("일별 랭킹 보상 지급 실패 - 유저 없음. rank={}, userId={}", i + 1, userId);
                continue;
            }

            userRepositoryPort.save(user.addCookie(cookies));
            log.info("일별 랭킹 보상 지급 완료. date={}, rank={}, userId={}, cookies={}", yesterday, i + 1, userId, cookies);
        }
    }
}