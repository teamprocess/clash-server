package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.util.RecordDayWindow;
import com.process.clash.application.record.v2.data.GetActivityStatisticsV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidActivityStatisticsDurationException;
import com.process.clash.application.record.v2.port.in.GetActivityStatisticsV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetActivityStatisticsV2Service implements GetActivityStatisticsV2UseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;
    private final Clock clock;

    @Override
    public GetActivityStatisticsV2Data.Result execute(GetActivityStatisticsV2Data.Command command) {
        userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        DurationWindow durationWindow = resolveDurationWindow(command.duration());
        Instant startTime = durationWindow.startTime().atZone(recordZoneId).toInstant();
        Instant endTime = durationWindow.endTime().atZone(recordZoneId).toInstant();

        List<GetActivityStatisticsV2Data.AppActivity> apps =
            recordDevelopSessionSegmentV2RepositoryPort.findAppActivityTotalsByUserIdAndRange(
                    command.actor().id(),
                    startTime,
                    endTime,
                    endTime
                ).stream()
                .map(row -> new GetActivityStatisticsV2Data.AppActivity(
                    row.appId().name(),
                    toAverageStudyTime(row.totalStudyTimeSeconds(), durationWindow.days())
                ))
                .toList();

        return GetActivityStatisticsV2Data.Result.from(apps);
    }

    private DurationWindow resolveDurationWindow(PeriodCategory duration) {
        if (duration == null) {
            throw new InvalidActivityStatisticsDurationException();
        }

        int days = switch (duration) {
            case DAY -> 1;
            case WEEK -> 7;
            case MONTH -> 30;
            default -> throw new InvalidActivityStatisticsDurationException();
        };

        RecordDayWindow todayWindow = RecordDayWindow.today(recordZoneId, recordProperties.dayBoundaryHour(), clock);
        LocalDateTime startTime = todayWindow.dayStart().minusDays(days - 1L);

        return new DurationWindow(startTime, todayWindow.endLimit(), days);
    }

    private Integer toAverageStudyTime(Long totalStudyTimeSeconds, int days) {
        long total = totalStudyTimeSeconds == null ? 0L : totalStudyTimeSeconds;
        return Math.toIntExact(total / days);
    }

    private record DurationWindow(
        LocalDateTime startTime,
        LocalDateTime endTime,
        int days
    ) {
    }
}
