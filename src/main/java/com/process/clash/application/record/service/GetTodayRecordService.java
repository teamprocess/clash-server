package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.out.SessionRepositoryPort;
import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.common.DateUtil;
import com.process.clash.domain.record.model.entity.Session;
import com.process.clash.domain.user.model.entity.User;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTodayRecordService implements GetTodayRecordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SessionRepositoryPort sessionRepositoryPort;

    public GetTodayRecordData.Result execute(GetTodayRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().userId())
            .orElseThrow(() -> new RuntimeException("유저 없음 (예외 런타인임 교체 부탁)"));
        String date = DateUtil.getCurrentDate();

        List<Session> sessionList = sessionRepositoryPort.findAllByUserId(user.id());
        List<Session> endedSessions = sessionList.stream()
            .filter(s -> s.endedAt() != null)
            .toList();

        long totalStudyTime = endedSessions.stream()
            .mapToLong(s -> ChronoUnit.MILLIS.between(s.startedAt(), s.endedAt()))
            .sum();
        Instant studyStoppedAt = endedSessions.stream()
            .max(Comparator.comparing(Session::endedAt))
            .map(s -> s.endedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant())
            .orElse(null);

        return GetTodayRecordData.Result.create(
            date,
            user.pomodoroEnabled(),
            totalStudyTime,
            studyStoppedAt
        );
    }
}
