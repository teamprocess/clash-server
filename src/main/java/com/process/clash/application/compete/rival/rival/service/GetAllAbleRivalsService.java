package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import com.process.clash.application.compete.rival.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    private static final int MAX_RIVAL_COUNT = 4;

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        Long myId = command.actor().id();

        if (rivalRepositoryPort.countAcceptedByUserId(myId) >= MAX_RIVAL_COUNT) {
            return GetAllAbleRivalsData.Result.from(List.of());
        }

        List<Rival> rivals = rivalRepositoryPort.findAllRivalsByUserId(myId);

        List<Long> excludedUserIds = Stream.concat(
                rivals.stream()
                        .filter(rival -> rival.rivalLinkingStatus() == RivalLinkingStatus.PENDING
                                || rival.rivalLinkingStatus() == RivalLinkingStatus.ACCEPTED)
                        .map(rival -> {
                            if (rival.firstUserId().equals(myId)) {
                                return rival.secondUserId();
                            }
                            return rival.firstUserId();
                        }),
                Stream.of(myId)
        ).distinct().toList();

        List<AbleRivalInfoForRival> candidates = userRepositoryPort.findAbleRivalsWithUserInfo(excludedUserIds);

        if (candidates.isEmpty()) {
            return GetAllAbleRivalsData.Result.from(List.of());
        }

        List<Long> candidateIds = candidates.stream().map(AbleRivalInfoForRival::id).toList();
        Map<Long, Integer> acceptedCounts = rivalRepositoryPort.countAcceptedByUserIdsGrouped(candidateIds)
                .stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("user_id")).longValue(),
                        map -> ((Number) map.get("count")).intValue()
                ));

        List<AbleRivalInfoForRival> ableRivalInfoForRivals = candidates.stream()
                .filter(user -> acceptedCounts.getOrDefault(user.id(), 0) < MAX_RIVAL_COUNT)
                .toList();

        return GetAllAbleRivalsData.Result.from(ableRivalInfoForRivals);
    }
}
