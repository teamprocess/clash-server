package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByMy_Id(command.actor().id());

        List<Long> opponentIds = rivals.stream()
                .map(Rival::opponentId)
                .toList();

        List<UserGitHub> userGitHubs = userGitHubRepositoryPort.findByIdNotIn(opponentIds);

        List<User> users = userRepositoryPort.findByIdIn(
                userGitHubs.stream().map(UserGitHub::userId).toList()
        );

        return GetAllAbleRivalsData.Result.from(users, userGitHubs);
    }
}
