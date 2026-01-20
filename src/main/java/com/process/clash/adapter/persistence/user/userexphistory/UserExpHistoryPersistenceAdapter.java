package com.process.clash.adapter.persistence.user.userexphistory;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
    public List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findDailyDataByUserIds(ids, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findWeeklyDataByUserIds(ids, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userExpHistoryJpaRepository.findMonthlyDataByUserIds(ids, startDate, endDate);
    }
}
