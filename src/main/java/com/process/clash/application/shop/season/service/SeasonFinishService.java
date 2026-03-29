package com.process.clash.application.shop.season.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeasonFinishService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    @Transactional
    public void resetAllUsersExp() {
        List<User> users = userRepositoryPort.findAllOrderByTotalExpDesc();

        if (users.isEmpty()) {
            log.info("시즌 리셋 대상 유저 없음");
            return;
        }

        log.info("시즌 리셋 시작. 대상 유저 수={}", users.size());

        LocalDate today = LocalDate.now();
        List<User> updatedUsers = new ArrayList<>();
        List<UserExpHistory> expHistories = new ArrayList<>();
        List<UserGoodsHistory> goodsHistories = new ArrayList<>();

        for (User user : users) {
            int currentExp = user.totalExp();
            if (currentExp <= 0) {
                continue;
            }

            int cookieReward = (int) Math.round(currentExp * 0.1);

            expHistories.add(new UserExpHistory(null, null, today, -currentExp, ExpActingCategory.SEASON_RESET, user.id()));

            if (cookieReward > 0) {
                goodsHistories.add(new UserGoodsHistory(null, null, GoodsActingCategory.SEASON_REWARD, cookieReward, null, user.id()));
            }

            updatedUsers.add(user.seasonReset(cookieReward));

            log.debug("시즌 리셋 처리. userId={}, exp={}, cookieReward={}", user.id(), currentExp, cookieReward);
        }

        if (!updatedUsers.isEmpty()) {
            userRepositoryPort.saveAll(updatedUsers);
        }
        if (!expHistories.isEmpty()) {
            userExpHistoryRepositoryPort.saveAll(expHistories);
        }
        if (!goodsHistories.isEmpty()) {
            userGoodsHistoryRepositoryPort.saveAll(goodsHistories);
        }

        log.info("시즌 리셋 완료. 처리 유저 수={}, 쿠키 지급 수={}", updatedUsers.size(), goodsHistories.size());
    }
}