package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import com.process.clash.application.record.v2.port.in.GetTodayRecordV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTodayRecordV2Service implements GetTodayRecordV2UseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetTodayRecordV2Data.Result execute(GetTodayRecordV2Data.Command command) {
        userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDate todayRecordDate = RecordDateCalculator.recordDate(nowZoned, boundaryHour);
        LocalDate recordDate = command.date() == null ? todayRecordDate : command.date();
        String date = recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDay = recordDate.atTime(boundaryHour, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime endLimit = recordDate.equals(todayRecordDate) && now.isBefore(endOfDay)
            ? now
            : endOfDay;

        List<RecordSessionV2> todaySessions = recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            startOfDay,
            endOfDay
        );

        long totalStudyTime = todaySessions.stream()
            .mapToLong(session -> sessionSecondsInWindow(session, startOfDay, endLimit))
            .sum();

        Instant studyStoppedAt = todaySessions.stream()
            .filter(session -> session.endedAt() != null)
            .map(session -> {
                LocalDateTime sessionEnd = toLocalDateTime(session.endedAt());
                LocalDateTime cappedEnd = sessionEnd.isAfter(endLimit) ? endLimit : sessionEnd;
                return cappedEnd.isAfter(startOfDay) ? cappedEnd : null;
            })
            .filter(cappedEnd -> cappedEnd != null)
            .max(Comparator.naturalOrder())
            .map(localDateTime -> localDateTime.atZone(recordZoneId).toInstant())
            .orElse(null);

        return GetTodayRecordV2Data.Result.from(
            date,
            totalStudyTime,
            studyStoppedAt,
            todaySessions.stream()
                .map(session -> {
                    LocalDateTime sessionStartLocal = toLocalDateTime(session.startedAt());
                    LocalDateTime sessionStart = sessionStartLocal.isAfter(startOfDay)
                        ? sessionStartLocal
                        : startOfDay;
                    LocalDateTime sessionEndLocal = session.endedAt() == null ? null : toLocalDateTime(session.endedAt());
                    LocalDateTime sessionEnd = sessionEndLocal == null
                        ? null
                        : (sessionEndLocal.isAfter(endLimit) ? endLimit : sessionEndLocal);
                    Instant sessionStartInstant = sessionStart.atZone(recordZoneId).toInstant();
                    Instant sessionEndInstant = sessionEnd == null ? null : sessionEnd.atZone(recordZoneId).toInstant();
                    return RecordSessionV2Mapper.toSession(session, sessionStartInstant, sessionEndInstant);
                })
                .toList()
        );
    }

    private long sessionSecondsInWindow(
        RecordSessionV2 session,
        LocalDateTime dayStart,
        LocalDateTime endLimit
    ) {
        LocalDateTime sessionStart = toLocalDateTime(session.startedAt());
        LocalDateTime effectiveStart = sessionStart.isAfter(dayStart) ? sessionStart : dayStart;
        LocalDateTime sessionEnd = session.endedAt() == null ? null : toLocalDateTime(session.endedAt());
        LocalDateTime effectiveEnd = sessionEnd == null ? endLimit : sessionEnd;

        if (effectiveEnd.isAfter(endLimit)) {
            effectiveEnd = endLimit;
        }
        if (!effectiveEnd.isAfter(effectiveStart)) {
            return 0L;
        }
        return ChronoUnit.SECONDS.between(effectiveStart, effectiveEnd);
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, recordZoneId);
    }
}
