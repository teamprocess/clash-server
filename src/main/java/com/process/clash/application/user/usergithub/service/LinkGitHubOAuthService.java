package com.process.clash.application.user.usergithub.service;

import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergithub.data.LinkGitHubOAuthData;
import com.process.clash.application.user.usergithub.exception.exception.badrequest.GithubOAuthInvalidCodeException;
import com.process.clash.application.user.usergithub.exception.exception.conflict.GithubAlreadyLinkedException;
import com.process.clash.application.user.usergithub.model.GithubOAuthToken;
import com.process.clash.application.user.usergithub.model.GithubOAuthUser;
import com.process.clash.application.user.usergithub.port.in.LinkGitHubOAuthUsecase;
import com.process.clash.application.user.usergithub.port.out.GithubOAuthPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkGitHubOAuthService implements LinkGitHubOAuthUsecase {

    private final UserRepositoryPort userRepositoryPort;
    private final GithubOAuthPort githubOAuthPort;
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    @Transactional
    public LinkGitHubOAuthData.Result execute(LinkGitHubOAuthData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);
        if (command.code() == null || command.code().isBlank()) {
            throw new GithubOAuthInvalidCodeException();
        }

        GithubOAuthToken token = githubOAuthPort.exchangeCodeForAccessToken(command.code());
        GithubOAuthUser githubUser = githubOAuthPort.fetchUser(token.accessToken());
        List<String> emails = githubOAuthPort.fetchEmails(token.accessToken());

        Optional<UserGitHub> existingByGithubId = userGitHubRepositoryPort.findByGitHubId(githubUser.login());
        if (existingByGithubId.isPresent() && !existingByGithubId.get().userId().equals(user.id())) {
            throw new GithubAlreadyLinkedException();
        }

        Optional<UserGitHub> existingByUserId = userGitHubRepositoryPort.findByUserId(user.id());
        UserGitHub existingLink = existingByUserId.orElse(existingByGithubId.orElse(null));
        UserGitHub userGitHub = new UserGitHub(
                existingLink != null ? existingLink.id() : null,
                githubUser.login(),
                user.id(),
                githubUser.nodeId(),
                joinEmails(emails),
                token.accessToken()
        );

        UserGitHub saved = userGitHubRepositoryPort.save(userGitHub);
        return new LinkGitHubOAuthData.Result(saved.gitHubId(), true);
    }

    private String joinEmails(List<String> emails) {
        if (emails == null || emails.isEmpty()) {
            return null;
        }
        return emails.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .reduce((left, right) -> left + "," + right)
                .orElse(null);
    }
}
