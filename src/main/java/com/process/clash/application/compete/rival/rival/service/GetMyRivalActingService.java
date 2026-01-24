package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.rival.port.in.GetMyRivalActingUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalCurrentStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.infrastructure.config.RecordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyRivalActingService implements GetMyRivalActingUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByUserId(command.actor().id());

        if (rivals.isEmpty()) {
            return GetMyRivalActingData.Result.from(List.of());
        }

        int boundaryHour = recordProperties.dayBoundaryHour();
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        LocalDate recordDate = nowZoned.toLocalDate();
        if (nowZoned.getHour() < boundaryHour) {
            recordDate = recordDate.minusDays(1);
        }
        LocalDateTime startOfDay = recordDate.atTime(boundaryHour, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Long> opponentIds = Stream.concat(
                rivals.stream()
                        .map(rival -> {
                            if (rival.firstUserId().equals(command.actor().id())) {
                                return rival.secondUserId();
                            }
                            return rival.firstUserId();
                        }),
                Stream.of(command.actor().id())
        ).distinct().toList();


        Map<Long, User> opponentMap = userRepositoryPort.findAllByIds(opponentIds)
                .stream()
                .collect(Collectors.toMap(User::id, user -> user));

        Map<Long, Long> studyTimeMap = studySessionRepositoryPort
                .getTotalStudyTimeInSecondsByUserIds(opponentIds, startOfDay, endOfDay);

        Long myId = command.actor().id();

        List<GetMyRivalActingData.MyRival> myRivals = rivals.stream()
                .map(rival -> {

                    Long opponentId;
                    if (rival.firstUserId().equals(myId)) {
                        opponentId = rival.secondUserId();
                    } else {
                        opponentId = rival.firstUserId();
                    }

                    User opponent = opponentMap.get(opponentId);
                    if (opponent == null) {
                        throw new UserNotFoundException();
                    }

                    Long activeTime = studyTimeMap.getOrDefault(opponentId, 0L);

                    return GetMyRivalActingData.MyRival.from(
                            opponent,
                            activeTime,
                            "Intellij IDEA",      // TODO: 더미 수정 필요
                            RivalCurrentStatus.ONLINE // TODO: 더미 수정 필요
                    );
                })
                .toList();


        return GetMyRivalActingData.Result.from(myRivals);
    }
}
