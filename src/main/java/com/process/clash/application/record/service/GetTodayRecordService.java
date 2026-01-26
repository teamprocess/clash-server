package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.infrastructure.config.RecordProperties;
import com.process.clash.application.user.userpomodorosetting.exception.exception.notfound.UserPomodoroSettingNotFoundException;
import com.process.clash.application.user.userpomodorosetting.port.out.UserPomodoroSettingRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.user.user.entity.User;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import com.process.clash.domain.user.userpomodorosetting.entity.UserPomodoroSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTodayRecordService implements GetTodayRecordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserPomodoroSettingRepositoryPort userPomodoroSettingRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public GetTodayRecordData.Result execute(GetTodayRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDate recordDate = nowZoned.toLocalDate();
        if (nowZoned.getHour() < boundaryHour) {
            recordDate = recordDate.minusDays(1);
        }
        String date = recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDay = recordDate.atTime(boundaryHour, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime endLimit = now.isBefore(endOfDay) ? now : endOfDay;

        List<StudySession> todaySessions = studySessionRepositoryPort.findAllByUserIdAndTimeRange(
            user.id(),
            startOfDay,
            endOfDay
        );

        long totalStudyTime = todaySessions.stream()
            .mapToLong(s -> {
                LocalDateTime effectiveStart = s.startedAt().isAfter(startOfDay) ? s.startedAt() : startOfDay;
                LocalDateTime effectiveEnd = s.endedAt() == null ? endLimit : s.endedAt();
                if (effectiveEnd.isAfter(endLimit)) {
                    effectiveEnd = endLimit;
                }
                if (!effectiveEnd.isAfter(effectiveStart)) {
                    return 0L;
                }
                return ChronoUnit.MILLIS.between(effectiveStart, effectiveEnd);
            })
            .sum();
        Instant studyStoppedAt = todaySessions.stream()
            .filter(s -> s.endedAt() != null)
            .map(s -> {
                LocalDateTime cappedEnd = s.endedAt().isAfter(endLimit) ? endLimit : s.endedAt();
                return cappedEnd.isAfter(startOfDay) ? cappedEnd : null;
            })
            .filter(e -> e != null)
            .max(Comparator.naturalOrder())
            .map(e -> e.atZone(recordZoneId).toInstant())
            .orElse(null);

        UserPomodoroSetting userPomodoroSetting = userPomodoroSettingRepositoryPort.findByUserId(user.id())
                .orElseThrow(UserPomodoroSettingNotFoundException::new);

        return GetTodayRecordData.Result.create(
            date,
            userPomodoroSetting.pomodoroEnabled(),
            totalStudyTime,
            studyStoppedAt,
            todaySessions.stream()
                .map(s -> GetTodayRecordData.Session.from(
                    (s.startedAt().isAfter(startOfDay) ? s.startedAt() : startOfDay).atZone(recordZoneId).toInstant(),
                    s.endedAt() == null ? null : (s.endedAt().isAfter(endLimit) ? endLimit : s.endedAt()).atZone(recordZoneId).toInstant(),
                    s.task().id(),
                    s.task().name()
                ))
                .toList()
        );
    }
}
