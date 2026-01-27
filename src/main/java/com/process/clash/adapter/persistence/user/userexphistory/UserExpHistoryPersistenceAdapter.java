package com.process.clash.adapter.persistence.user.userexphistory;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.UserEarnedExp;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserExpHistoryPersistenceAdapter implements UserExpHistoryRepositoryPort {

    private final UserExpHistoryJpaRepository userExpHistoryJpaRepository;
    private final UserExpHistoryJpaMapper userExpHistoryJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserExpHistory save(UserExpHistory userExpHistory) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userExpHistory.userId());
        UserExpHistoryJpaEntity savedEntity = userExpHistoryJpaRepository.save(userExpHistoryJpaMapper.toJpaEntity(userExpHistory, userJpaEntity));
        return userExpHistoryJpaMapper.toDomain(savedEntity);
    }

    @Override
    public List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findDailyDataByUserIds(ids, startDate, endDate, pageRequest);
    }

    @Override
    public List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findWeeklyDataByUserIds(ids, startDate, endDate, pageRequest);
    }

    @Override
    public List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findMonthlyDataByUserIds(ids, startDate, endDate, pageRequest);
    }

    @Override
    public double findAverageExpByUserIdAndCategoryAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findAverageExpByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Override
    public Map<Long, Double> findAverageExpForBattles(Long userId, List<Battle> battles) {
        if (battles.isEmpty()) {
            return new HashMap<>();
        }

        List<Long> battleIds = battles.stream()
                .map(Battle::id)
                .toList();

        List<Object[]> averages = userExpHistoryJpaRepository.findAverageExpForBattles(userId, battleIds);

        return averages.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).doubleValue()
                ));
    }

    @Override
    public List<Streak> findStreakByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findStreakByUserId(userId, startDate, endDate);
    }

    @Override
    public List<Variation> findVariationByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findVariationByUserId(userId, startDate, endDate);
    }

    @Override
    public List<UserEarnedExp> findUserDailyEarnedExpByUserIdAndPeriod(Long id, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findUserDailyEarnedExpByUserIdAndPeriod(id, startDate, endDate, pageRequest);
    }

    @Override
    public List<UserEarnedExp> findUserWeeklyEarnedExpByUserIdAndPeriod(Long id, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findUserWeeklyEarnedExpByUserIdAndPeriod(id, startDate, endDate, pageRequest);
    }

    @Override
    public List<UserEarnedExp> findUserMonthlyEarnedExpByUserIdAndPeriod(Long id, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userExpHistoryJpaRepository.findUserMonthlyEarnedExpByUserIdAndPeriod(id, startDate, endDate, pageRequest);
    }

    @Override
    public List<UserRanking> findExpRankingByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findExpRankingByUserIdAndPeriod(userId, startDate, endDate);
    }
}
