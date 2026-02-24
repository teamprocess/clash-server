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
        int myRivalCount = rivalRepositoryPort.countAllByUserId(command.actor().id());

        if (myRivalCount + command.ids().size() > MAX_RIVAL_COUNT) {
            throw new TooMuchRivalsException();
        }

        List<Long> opponentIds = command.ids().stream()
                .map(id -> id.id())
                .collect(Collectors.toList());

        boolean hasAlreadyApplied = opponentIds.stream()
                .anyMatch(opponentId -> rivalRepositoryPort.existsActiveRivalBetween(command.actor().id(), opponentId));

        if (hasAlreadyApplied) {
            throw new AlreadyAppliedRivalException();
        }

        Map<Long, Integer> opponentRivalCounts =
                rivalRepositoryPort.countAllByOpponentIdsGrouped(opponentIds).stream()
                        .collect(Collectors.toMap(
                                map -> (Long) map.get("opponentId"),
                                map -> ((Long) map.get("count")).intValue()
                        ));

        boolean hasOverLimitRival = opponentRivalCounts.values().stream()
                .anyMatch(count -> count >= MAX_RIVAL_COUNT);

        if (hasOverLimitRival) {
            throw new TooMuchRivalsException();
        }
    }
}