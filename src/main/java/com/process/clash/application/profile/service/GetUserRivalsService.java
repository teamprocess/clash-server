package com.process.clash.application.profile.service;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.data.GetUserRivalsData;
import com.process.clash.application.profile.exception.exception.forbidden.ProfilePrivatedException;
import com.process.clash.application.profile.port.in.GetUserRivalsUsecase;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserRivalsService implements GetUserRivalsUsecase {

    private final UserRepositoryPort userRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    private final UserPresencePort userPresencePort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;
    private final EquippedItemsAssembler equippedItemsAssembler;

    @Override
    public GetMyRivalActingData.Result execute(GetUserRivalsData.Command command) {
        User targetUser = userRepositoryPort.findById(command.targetUserId())
            .orElseThrow(UserNotFoundException::new);

        if (targetUser.isDeleted()) {
            throw new UserNotFoundException();
        }

        if (targetUser.isPrivate()) {
            throw new ProfilePrivatedException();
        }

        List<Rival> rivals = rivalRepositoryPort.findAllByUserId(command.targetUserId());

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

        List<Long> opponentIds = rivals.stream()
            .map(rival -> {
                if (rival.firstUserId().equals(command.targetUserId())) {
                    return rival.secondUserId();
                }
                return rival.firstUserId();
            })
            .distinct()
            .toList();

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

        Map<Long, EquippedItemsData> equippedItemsMap = equippedItemsAssembler.loadByUserIds(opponentIds);

        List<GetMyRivalActingData.MyRival> myRivals = rivals.stream()
            .map(rival -> {
                Long opponentId = rival.firstUserId().equals(command.targetUserId())
                    ? rival.secondUserId()
                    : rival.firstUserId();

                User opponent = opponentMap.get(opponentId);
                if (opponent == null) {
                    return null;
                }

                Long activeTime = studyTimeMap.getOrDefault(opponentId, 0L);
                UserActivityStatus activityStatus = activityStatusByUserId.getOrDefault(opponentId, UserActivityStatus.OFFLINE);
                RecordSessionV2 activeSession = activeSessionByUserId.get(opponentId);
                String usingApp = activeSession != null ? activeSession.resolveUsingApp() : null;
                boolean isStudying = activeSession != null;
                EquippedItemsData equippedItems = equippedItemsMap.getOrDefault(opponentId, EquippedItemsData.empty());

                return GetMyRivalActingData.MyRival.of(
                    rival.id(), opponent, activeTime, usingApp, isStudying, activityStatus, equippedItems
                );
            })
            .filter(Objects::nonNull)
            .toList();

        return GetMyRivalActingData.Result.from(myRivals);
    }

}
