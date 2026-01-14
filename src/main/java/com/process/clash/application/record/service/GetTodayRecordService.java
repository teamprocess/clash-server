package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.out.SessionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.common.DateUtil;
import com.process.clash.domain.record.model.entity.Session;
import com.process.clash.domain.user.user.model.entity.User;
import java.time.Instant;
import java.time.ZoneOffset;
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

        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);
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
            .map(s -> s.endedAt().atZone(ZoneOffset.UTC).toInstant())
            .orElse(null);

        return GetTodayRecordData.Result.create(
            date,
            user.pomodoroEnabled(),
            totalStudyTime,
            studyStoppedAt
        );
    }
}
