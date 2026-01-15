package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.common.DateUtil;
import com.process.clash.domain.record.model.entity.StudySession;
import com.process.clash.domain.user.user.entity.User;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final StudySessionRepositoryPort studySessionRepositoryPort;

    public GetTodayRecordData.Result execute(GetTodayRecordData.Command command) {

        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);
        String date = DateUtil.getCurrentDate();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        List<StudySession> todaySessions = studySessionRepositoryPort.findAllByUserIdAndStartedAtAfter(user.id(), today);

        long totalStudyTime = todaySessions.stream()
            .mapToLong(s -> ChronoUnit.MILLIS.between(
                s.startedAt(),
                s.endedAt() == null ? now : s.endedAt()
            ))
            .sum();
        Instant studyStoppedAt = todaySessions.stream()
            .filter(s -> s.endedAt() != null)
            .max(Comparator.comparing(StudySession::endedAt))
            .map(s -> s.endedAt().atZone(ZoneOffset.UTC).toInstant())
            .orElse(null);

        return GetTodayRecordData.Result.create(
            date,
            user.pomodoroEnabled(),
            totalStudyTime,
            studyStoppedAt,
            todaySessions.stream()
                .map(s -> GetTodayRecordData.Session.from(
                    s.startedAt(),
                    s.endedAt(),
                    s.task().id(),
                    s.task().name()
                ))
                .toList()
        );
    }
}
