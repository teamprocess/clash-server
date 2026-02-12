package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.my.data.Streak;
import com.process.clash.application.compete.my.data.Variation;
import com.process.clash.application.user.userstudytime.data.UserStudyTimeDailyDto;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserStudyTimePersistenceAdapter implements UserStudyTimeRepositoryPort {

    private final UserStudyTimeJpaRepository userStudyTimeJpaRepository;
    private final UserStudyTimeJpaMapper userStudyTimeJpaMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserStudyTime save(UserStudyTime userStudyTime) {

        UserJpaEntity userJpaEntity = userJpaRepository.getReferenceById(userStudyTime.userId());
        UserStudyTimeJpaEntity savedEntity = userStudyTimeJpaRepository.save(userStudyTimeJpaMapper.toJpaEntity(userStudyTime, userJpaEntity));
        return userStudyTimeJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserStudyTime> findByUserIdAndDate(Long userId, LocalDate date) {

        return userStudyTimeJpaRepository.findByUserIdAndDate(userId, date)
                .map(userStudyTimeJpaMapper::toDomain);
    }

    @Override
    public List<UserStudyTimeDailyDto> findDailyDataByUserId(Long userId, LocalDate startDate, LocalDate endDate, PageRequest pageRequest) {

        return userStudyTimeJpaRepository.findDailyDataByUserId(userId, startDate, endDate, pageRequest);
    }

    @Override
    public List<Object[]> findDailyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findDailyDataByUserIds(ids, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findWeeklyDataByUserIds(ids, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyDataByUserIds(List<Long> ids, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findMonthlyDataByUserIds(ids, startDate, endDate);
    }

    @Override
    public double findAverageStudyTimeByUserIdAndPeriod(Long userId, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findAverageStudyTimeByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Override
    public List<Streak> findStreakByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findStreakByUserId(userId, startDate, endDate);
    }

    @Override
    public List<Variation> findVariationByUserId(Long userId, LocalDate startDate, LocalDate endDate) {

        return userStudyTimeJpaRepository.findVariationByUserId(userId, startDate, endDate);
    }
}
