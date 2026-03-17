package com.process.clash.application.ranking.service;

import com.process.clash.application.github.service.StudyDateCalculator;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZeroRankingDataInitService {

    private final UserRepositoryPort userRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final StudyDateCalculator studyDateCalculator;

    /**
     * 오늘 EXP 기록이 없는 유저에게 0 EXP 레코드를 삽입하여 랭킹 조회에 포함되도록 한다.
     */
    @Transactional
    public void initZeroExpForToday() {
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.now());
        List<User> allUsers = userRepositoryPort.findAllOrderByTotalExpDesc();
        Set<Long> userIdsWithExp = userExpHistoryRepositoryPort.findUserIdsWithExpByDate(studyDate);

        List<UserExpHistory> zeroRecords = allUsers.stream()
                .filter(user -> !userIdsWithExp.contains(user.id()))
                .map(user -> new UserExpHistory(null, Instant.now(), studyDate, 0, ExpActingCategory.STUDY_TIME, user.id()))
                .toList();

        if (!zeroRecords.isEmpty()) {
            userExpHistoryRepositoryPort.saveAll(zeroRecords);
        }
        log.info("0 EXP 초기화 완료. date={}, count={}", studyDate, zeroRecords.size());
    }

    /**
     * 오늘 학습시간 기록이 없는 유저에게 0초짜리 레코드를 삽입하여 랭킹 조회에 포함되도록 한다.
     */
    @Transactional
    public void initZeroStudyTimeForToday() {
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.now());
        List<User> allUsers = userRepositoryPort.findAllOrderByTotalExpDesc();
        Set<Long> userIdsWithStudyTime = userStudyTimeRepositoryPort.findUserIdsWithStudyTimeByDate(studyDate);

        List<UserStudyTime> zeroRecords = allUsers.stream()
                .filter(user -> !userIdsWithStudyTime.contains(user.id()))
                .map(user -> new UserStudyTime(null, studyDate, 0L, user.id()))
                .toList();

        if (!zeroRecords.isEmpty()) {
            userStudyTimeRepositoryPort.saveAll(zeroRecords);
        }
        log.info("0 학습시간 초기화 완료. date={}, count={}", studyDate, zeroRecords.size());
    }
}
