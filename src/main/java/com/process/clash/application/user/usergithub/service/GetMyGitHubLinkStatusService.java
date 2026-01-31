package com.process.clash.application.user.usergithub.service;

import com.process.clash.application.user.usergithub.data.GetMyGitHubLinkStatusData;
import com.process.clash.application.user.usergithub.port.in.GetMyGitHubLinkStatusUsecase;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMyGitHubLinkStatusService implements GetMyGitHubLinkStatusUsecase {

    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetMyGitHubLinkStatusData.Result execute(GetMyGitHubLinkStatusData.Command command) {
        Optional<UserGitHub> userGitHub = userGitHubRepositoryPort.findByUserId(command.actor().id());
        return new GetMyGitHubLinkStatusData.Result(
                userGitHub.isPresent(),
                userGitHub.map(UserGitHub::gitHubId).orElse(null)
        );
    }
}
