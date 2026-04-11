package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.rival.port.in.GetMyRivalActingUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.service.EquippedItemsAssembler;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.infrastructure.config.record.RecordProperties;
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
    private final RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserPresencePort userPresencePort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;
    private final EquippedItemsAssembler equippedItemsAssembler;

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

        Map<Long, Long> studyTimeMap = recordSessionRepositoryPort
                .getTotalStudyTimeInSecondsByUserIds(opponentIds, startOfDay, endOfDay);

        Map<Long, RecordSessionV2> activeSessionByUserId = recordSessionRepositoryPort
                .findAllActiveSessionsByUserIds(opponentIds)
                .stream()
                .collect(Collectors.toMap(
                        RecordSessionV2::userId,
                        session -> session,
                        (first, second) -> first
                ));

        Map<Long, UserActivityStatus> activityStatusByUserId = userPresencePort.getStatuses(opponentIds);

        List<Long> rivalIds = opponentIds.stream()
                .filter(id -> !id.equals(command.actor().id()))
                .toList();
        Map<Long, EquippedItemsData> equippedItemsMap = equippedItemsAssembler.loadByUserIds(rivalIds);

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
                    UserActivityStatus activityStatus = activityStatusByUserId.getOrDefault(
                        opponentId,
                        UserActivityStatus.OFFLINE
                    );
                    RecordSessionV2 activeSession = activeSessionByUserId.get(opponentId);
                    String usingApp = resolveUsingApp(activeSession);
                    boolean isStudying = activeSession != null;

                    EquippedItemsData equippedItems = equippedItemsMap.getOrDefault(opponentId, EquippedItemsData.empty());

                    return GetMyRivalActingData.MyRival.of(
                            rival.id(),
                            opponent,
                            activeTime,
                            usingApp,
                            isStudying,
                            activityStatus,
                            equippedItems
                    );
                })
                .toList();


        return GetMyRivalActingData.Result.from(myRivals);
    }

    private String resolveUsingApp(RecordSessionV2 activeSession) {
        if (activeSession == null) {
            return null;
        }
        if (activeSession.sessionType() != RecordSessionTypeV2.DEVELOP) {
            return null;
        }
        if (activeSession.appId() == null) {
            return null;
        }
        return activeSession.appId().name();
    }
}
