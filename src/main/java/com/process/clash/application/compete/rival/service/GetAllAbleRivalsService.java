package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByMyId(command.actor().id());

        List<Long> excludedUserIds = Stream.concat(
                rivals.stream().map(Rival::opponentId),
                Stream.of(command.actor().id())
        ).toList();

        List<AbleRivalInfo> ableRivalInfos = userGitHubRepositoryPort.findAbleRivalsWithUserInfo(excludedUserIds);

        return GetAllAbleRivalsData.Result.from(ableRivalInfos);
    }
}
