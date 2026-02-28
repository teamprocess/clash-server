package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.FindAllRivalsData;
import com.process.clash.application.compete.rival.rival.port.in.FindAllRivalsUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllRivalsService implements FindAllRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public FindAllRivalsData.Result execute(FindAllRivalsData.Command command) {

        Long myId = command.actor().id();
        List<Rival> rivals = rivalRepositoryPort.findAllRivalsByUserId(myId);

        List<Long> opponentIds = rivals.stream()
                .map(rival -> rival.firstUserId().equals(myId)
                        ? rival.secondUserId()
                        : rival.firstUserId())
                .distinct()
                .toList();

        Map<Long, User> userMap = userRepositoryPort.findAllByIds(opponentIds)
                .stream()
                .collect(Collectors.toMap(User::id, user -> user));

        List<FindAllRivalsData.RivalInfo> rivalInfos = rivals.stream()
                .map(rival -> {
                    Long opponentId = rival.firstUserId().equals(myId)
                            ? rival.secondUserId()
                            : rival.firstUserId();
                    User opponent = userMap.get(opponentId);
                    return FindAllRivalsData.RivalInfo.of(rival.id(), opponent, rival.rivalLinkingStatus());
                })
                .sorted(Comparator.comparing(info ->
                        info.rivalLinkingStatus() != RivalLinkingStatus.ACCEPTED))
                .toList();

        return FindAllRivalsData.Result.from(rivalInfos);
    }
}
