package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.port.in.GetMyRivalActingUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import com.process.clash.domain.rival.enums.RivalCurrentStatus;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMyRivalActingService implements GetMyRivalActingUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private static final int DAY_START_HOUR = 6;

    @Override
    public GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByMyId(command.actor().id());

        if (rivals.isEmpty()) {
            return GetMyRivalActingData.Result.from(List.of());
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(DAY_START_HOUR, 0);
        LocalDateTime endOfDay = today.plusDays(1).atTime(DAY_START_HOUR, 0);

        List<Long> opponentIds = rivals.stream()
                .map(Rival::opponentId)
                .toList();

        Map<Long, User> opponentMap = userRepositoryPort.findAllByIds(opponentIds)
                .stream()
                .collect(Collectors.toMap(User::id, user -> user));

        Map<Long, Long> studyTimeMap = studySessionRepositoryPort
                .getTotalStudyTimeInSecondsByUserIds(opponentIds, startOfDay, endOfDay);

        List<GetMyRivalActingData.MyRival> myRivals = rivals.stream()
                .map(rival -> {

                    Long activeTime = studySessionRepositoryPort
                            .getTotalStudyTimeInSeconds(
                                    rival.opponentId(),
                                    startOfDay,
                                    endOfDay
                            );

                    User opponent = userRepositoryPort.findById(rival.opponentId())
                            .orElseThrow(UserNotFoundException::new);

                    return GetMyRivalActingData.MyRival.from(
                            opponent,
                            activeTime == null ? 0L : activeTime,
                            "Intellij IDEA", //TODO: 더미 수정 필요
                            RivalCurrentStatus.ONLINE //TODO: 더미 수정 필요
                    );
                })
                .toList();

        return GetMyRivalActingData.Result.from(myRivals);
    }
}