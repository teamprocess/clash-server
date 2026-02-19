package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.infrastructure.config.RecordProperties;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTodayRecordService implements GetTodayRecordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public GetTodayRecordData.Result execute(GetTodayRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDate recordDate = RecordDateCalculator.recordDate(nowZoned, boundaryHour);
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
                LocalDateTime sessionStart = toLocalDateTime(s.startedAt());
                LocalDateTime effectiveStart = sessionStart.isAfter(startOfDay) ? sessionStart : startOfDay;
                LocalDateTime sessionEnd = s.endedAt() == null ? null : toLocalDateTime(s.endedAt());
                LocalDateTime effectiveEnd = sessionEnd == null ? endLimit : sessionEnd;
                if (effectiveEnd.isAfter(endLimit)) {
                    effectiveEnd = endLimit;
                }
                if (!effectiveEnd.isAfter(effectiveStart)) {
                    return 0L;
                }
                return ChronoUnit.SECONDS.between(effectiveStart, effectiveEnd);
            })
            .sum();
        Instant studyStoppedAt = todaySessions.stream()
            .filter(s -> s.endedAt() != null)
            .map(s -> {
                LocalDateTime sessionEnd = toLocalDateTime(s.endedAt());
                LocalDateTime cappedEnd = sessionEnd.isAfter(endLimit) ? endLimit : sessionEnd;
                return cappedEnd.isAfter(startOfDay) ? cappedEnd : null;
            })
            .filter(e -> e != null)
            .max(Comparator.naturalOrder())
            .map(localDateTime -> localDateTime.atZone(recordZoneId).toInstant())
            .orElse(null);

        return GetTodayRecordData.Result.create(
            date,
            totalStudyTime,
            studyStoppedAt,
            todaySessions.stream()
                .map(s -> {
                    LocalDateTime sessionStartLocal = toLocalDateTime(s.startedAt());
                    LocalDateTime sessionStart = sessionStartLocal.isAfter(startOfDay) ? sessionStartLocal : startOfDay;
                    LocalDateTime sessionEndLocal = s.endedAt() == null ? null : toLocalDateTime(s.endedAt());
                    LocalDateTime sessionEnd = sessionEndLocal == null
                        ? null
                        : (sessionEndLocal.isAfter(endLimit) ? endLimit : sessionEndLocal);
                    Instant sessionStartInstant = sessionStart.atZone(recordZoneId).toInstant();
                    Instant sessionEndInstant = sessionEnd == null ? null : sessionEnd.atZone(recordZoneId).toInstant();
                    return RecordSessionMapper.toSession(s, sessionStartInstant, sessionEndInstant);
                })
                .toList()
        );
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, recordZoneId);
    }
}
