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

        int count = 0;
        for (User user : allUsers) {
            boolean hasExpRecord =
                    userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(user.id(), studyDate, ExpActingCategory.STUDY_TIME).isPresent()
                    || userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(user.id(), studyDate, ExpActingCategory.GITHUB).isPresent();

            if (!hasExpRecord) {
                userExpHistoryRepositoryPort.save(new UserExpHistory(
                        null, Instant.now(), studyDate, 0, ExpActingCategory.STUDY_TIME, user.id()
                ));
                count++;
            }
        }
        log.info("0 EXP 초기화 완료. date={}, count={}", studyDate, count);
    }

    /**
     * 오늘 학습시간 기록이 없는 유저에게 0초짜리 레코드를 삽입하여 랭킹 조회에 포함되도록 한다.
     */
    @Transactional
    public void initZeroStudyTimeForToday() {
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.now());
        List<User> allUsers = userRepositoryPort.findAllOrderByTotalExpDesc();

        int count = 0;
        for (User user : allUsers) {
            boolean hasStudyTimeRecord =
                    userStudyTimeRepositoryPort.findByUserIdAndDate(user.id(), studyDate).isPresent();

            if (!hasStudyTimeRecord) {
                userStudyTimeRepositoryPort.save(new UserStudyTime(
                        null, studyDate, 0L, user.id()
                ));
                count++;
            }
        }
        log.info("0 학습시간 초기화 완료. date={}, count={}", studyDate, count);
    }
}