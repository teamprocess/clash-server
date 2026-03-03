package com.process.clash.application.user.exp.service;

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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyTimeExpGrantService {

    private static final int MAX_STUDY_MINUTES = 600;
    private static final long MAX_STUDY_SECONDS = MAX_STUDY_MINUTES * 60L;
    private static final int EXP_PER_MINUTE = 10;

    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final StudyDateCalculator studyDateCalculator;

    @Transactional
    public void grant(Long userId, Instant startedAt, Instant endedAt) {
        long durationSeconds = endedAt.getEpochSecond() - startedAt.getEpochSecond();
        if (durationSeconds <= 0) {
            return;
        }

        Optional<User> userOpt = userRepositoryPort.findByIdForUpdate(userId);
        if (userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();

        LocalDate studyDate = studyDateCalculator.toStudyDate(startedAt);

        // 1. user_study_times upsert (누적, 최대 36000초 = 600분)
        Optional<UserStudyTime> existingStudyTime = userStudyTimeRepositoryPort.findByUserIdAndDate(userId, studyDate);
        long currentSeconds = existingStudyTime.map(UserStudyTime::totalStudyTimeSeconds).orElse(0L);
        long newTotalSeconds = Math.min(currentSeconds + durationSeconds, MAX_STUDY_SECONDS);

        userStudyTimeRepositoryPort.save(new UserStudyTime(
                existingStudyTime.map(UserStudyTime::id).orElse(null),
                studyDate,
                newTotalSeconds,
                userId
        ));

        // 2. STUDY_TIME EXP 계산
        int newEarnExp = (int) (newTotalSeconds / 60) * EXP_PER_MINUTE;

        // 3. user_exp_history upsert
        Optional<UserExpHistory> existingExp = userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(
                userId, studyDate, ExpActingCategory.STUDY_TIME
        );

        int prevEarnExp = existingExp.map(UserExpHistory::earnExp).orElse(0);
        int delta = newEarnExp - prevEarnExp;
        if (delta == 0) {
            return;
        }

        userExpHistoryRepositoryPort.save(new UserExpHistory(
                existingExp.map(UserExpHistory::id).orElse(null),
                existingExp.map(UserExpHistory::createdAt).orElse(Instant.now()),
                studyDate,
                newEarnExp,
                ExpActingCategory.STUDY_TIME,
                userId
        ));

        // 4. user.totalExp 갱신
        userRepositoryPort.save(user.addExp(delta));

        log.debug("학습시간 EXP 지급 완료. userId={}, studyDate={}, durationSec={}, delta={}, newEarnExp={}",
                userId, studyDate, durationSeconds, delta, newEarnExp);
    }
}
