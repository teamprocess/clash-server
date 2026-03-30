package com.process.clash.application.ranking.service;

import com.process.clash.application.github.service.StudyDateCalculator;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZeroRankingDataInitService {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final StudyDateCalculator studyDateCalculator;

    /**
     * 오늘 EXP 기록이 없는 유저에게 0 EXP 레코드를 삽입하여 랭킹 조회에 포함되도록 한다.
     */
    @Transactional
    public void initZeroExpForToday() {
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.now());
        List<Long> userIdsWithoutExp = userExpHistoryRepositoryPort.findUserIdsWithoutExpOnDate(studyDate);

        List<UserExpHistory> zeroRecords = userIdsWithoutExp.stream()
                .map(userId -> new UserExpHistory(null, Instant.now(), studyDate, 0, ExpActingCategory.STUDY_TIME, userId))
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
        List<Long> userIdsWithoutStudyTime = userStudyTimeRepositoryPort.findUserIdsWithoutStudyTimeOnDate(studyDate);

        List<UserStudyTime> zeroRecords = userIdsWithoutStudyTime.stream()
                .map(userId -> new UserStudyTime(null, studyDate, 0L, userId))
                .toList();

        if (!zeroRecords.isEmpty()) {
            userStudyTimeRepositoryPort.saveAll(zeroRecords);
        }
        log.info("0 학습시간 초기화 완료. date={}, count={}", studyDate, zeroRecords.size());
    }
}
