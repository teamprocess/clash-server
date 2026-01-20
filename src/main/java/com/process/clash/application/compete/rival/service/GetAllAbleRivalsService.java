package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
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
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByUserId(command.actor().id());

        Long myId = command.actor().id();

        List<Long> excludedUserIds = Stream.concat(
                rivals.stream()
                        .map(rival -> {
                            if (rival.firstUserId().equals(myId)) {
                                return rival.secondUserId();
                            }
                            return rival.firstUserId();
                        }),
                Stream.of(myId)
        ).distinct().toList();


        List<AbleRivalInfo> ableRivalInfos = userGitHubRepositoryPort.findAbleRivalsWithUserInfo(excludedUserIds);

        return GetAllAbleRivalsData.Result.from(ableRivalInfos);
    }
}
