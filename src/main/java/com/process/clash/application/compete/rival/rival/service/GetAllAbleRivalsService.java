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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllRivalsByUserId(command.actor().id());

        Long myId = command.actor().id();

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


        List<AbleRivalInfoForRival> ableRivalInfoForRivals = userRepositoryPort.findAbleRivalsWithUserInfo(excludedUserIds);

        return GetAllAbleRivalsData.Result.from(ableRivalInfoForRivals);
    }
}
