package com.process.clash.application.compete.rival.policy;

import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
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