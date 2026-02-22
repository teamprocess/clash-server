package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.util.RecordDayWindow;
import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidRecordV2DailyDateRequestException;
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

        int boundaryHour = recordProperties.dayBoundaryHour();
        RecordDayWindow todayWindow = RecordDayWindow.today(recordZoneId, boundaryHour);
        LocalDate todayRecordDate = todayWindow.recordDate();
        LocalDate recordDate = command.date() == null ? todayRecordDate : command.date();
        // 미래 기록일 조회는 허용하지 않음
        if (recordDate.isAfter(todayRecordDate)) {
            throw new InvalidRecordV2DailyDateRequestException();
        }
        RecordDayWindow dayWindow = recordDate.equals(todayRecordDate)
            ? todayWindow
            : RecordDayWindow.of(recordDate, recordZoneId, boundaryHour);
        boolean isTodayRecordDate = dayWindow.todayRecordDate();
        String date = recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime startOfDay = dayWindow.dayStart();
        LocalDateTime endOfDay = dayWindow.dayEnd();
        LocalDateTime endLimit = dayWindow.endLimit();

        List<RecordSessionV2> todaySessions = recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            startOfDay,
            endOfDay
        );

        long totalStudyTime = todaySessions.stream()
            .mapToLong(session -> sessionSecondsInWindow(session, startOfDay, endLimit))
            .sum();

        // studyStoppedAt은 "오늘 화면"에서만 의미가 있어 과거 날짜는 null로 반환
        Instant studyStoppedAt = isTodayRecordDate
            ? todaySessions.stream()
                .filter(session -> session.endedAt() != null)
                .map(session -> {
                    LocalDateTime sessionEnd = toLocalDateTime(session.endedAt());
                    LocalDateTime cappedEnd = sessionEnd.isAfter(endLimit) ? endLimit : sessionEnd;
                    return cappedEnd.isAfter(startOfDay) ? cappedEnd : null;
                })
                .filter(cappedEnd -> cappedEnd != null)
                .max(Comparator.naturalOrder())
                .map(localDateTime -> localDateTime.atZone(recordZoneId).toInstant())
                .orElse(null)
            : null;

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
                    LocalDateTime sessionEnd = projectedSessionEnd(sessionEndLocal, endLimit, isTodayRecordDate);
                    Instant sessionStartInstant = sessionStart.atZone(recordZoneId).toInstant();
                    Instant sessionEndInstant = sessionEnd == null ? null : sessionEnd.atZone(recordZoneId).toInstant();
                    return RecordSessionV2Mapper.toSession(session, sessionStartInstant, sessionEndInstant);
                })
                .toList()
        );
    }

    private LocalDateTime projectedSessionEnd(
        LocalDateTime sessionEnd,
        LocalDateTime endLimit,
        boolean isTodayRecordDate
    ) {
        // 미종료 세션은 오늘이면 진행중(null), 과거면 해당 기록일 종료 시점으로 보정
        if (sessionEnd == null) {
            return isTodayRecordDate ? null : endLimit;
        }
        return sessionEnd.isAfter(endLimit) ? endLimit : sessionEnd;
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
