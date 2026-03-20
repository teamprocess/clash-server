package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.compete.rival.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.rival.exception.exception.conflict.AlreadyAppliedRivalException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplyRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;
    private static final int MAX_RIVAL_COUNT = 4;

    public void check(ApplyRivalData.Command command) {

        if (rivalRepositoryPort.countAcceptedByUserId(command.actor().id()) >= MAX_RIVAL_COUNT) {
            throw new TooMuchRivalsException();
        }

        List<Long> opponentIds = command.ids().stream()
                .map(id -> id.id())
                .toList();

        boolean hasAlreadyApplied = opponentIds.stream()
                .anyMatch(opponentId -> rivalRepositoryPort.existsActiveRivalBetween(command.actor().id(), opponentId));

        if (hasAlreadyApplied) {
            throw new AlreadyAppliedRivalException();
        }

        Map<Long, Integer> opponentAcceptedCounts = rivalRepositoryPort.countAcceptedByUserIdsGrouped(opponentIds)
                .stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("user_id")).longValue(),
                        map -> ((Number) map.get("count")).intValue()
                ));

        boolean hasOverLimitOpponent = opponentIds.stream()
                .anyMatch(opponentId -> opponentAcceptedCounts.getOrDefault(opponentId, 0) >= MAX_RIVAL_COUNT);

        if (hasOverLimitOpponent) {
            throw new TooMuchRivalsException();
        }
    }
}