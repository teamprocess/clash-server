package com.process.clash.adapter.github;

import com.process.clash.application.user.usergithub.exception.exception.badrequest.GithubOAuthInvalidCodeException;
import com.process.clash.application.user.usergithub.exception.exception.internalserver.GithubOAuthTokenRequestFailedException;
import com.process.clash.application.user.usergithub.exception.exception.internalserver.GithubOAuthUserFetchFailedException;
import com.process.clash.application.user.usergithub.model.GithubOAuthToken;
import com.process.clash.application.user.usergithub.model.GithubOAuthUser;
import com.process.clash.application.user.usergithub.port.out.GithubOAuthPort;
import com.process.clash.infrastructure.config.GithubOAuthProperties;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class GithubOAuthAdapter implements GithubOAuthPort {

    private final GithubOAuthProperties properties;

    private final WebClient githubOAuthWebClient;

    private final WebClient githubApiWebClient;

    public GithubOAuthAdapter(
            GithubOAuthProperties properties,
            @Qualifier("githubOAuthWebClient") WebClient githubOAuthWebClient,
            @Qualifier("githubApiWebClient") WebClient githubApiWebClient
    ) {
        this.properties = properties;
        this.githubOAuthWebClient = githubOAuthWebClient;
        this.githubApiWebClient = githubApiWebClient;
    }

    @Override
    public GithubOAuthToken exchangeCodeForAccessToken(String code) {
        try {
            GithubAccessTokenResponse response = githubOAuthWebClient.post()
                    .uri("/login/oauth/access_token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("client_id", properties.clientId())
                            .with("client_secret", properties.clientSecret())
                            .with("code", code))
                    .retrieve()
                    .bodyToMono(GithubAccessTokenResponse.class)
                    .block();

            if (response == null) {
                throw new GithubOAuthTokenRequestFailedException();
            }
            if (response.error() != null && !response.error().isBlank()) {
                log.warn("GitHub OAuth Error: {}: {} ({})", response.error(), response.error_description(), response.error_uri());
                throw new GithubOAuthInvalidCodeException();
            }
            if (response.access_token() == null || response.access_token().isBlank()) {
                throw new GithubOAuthTokenRequestFailedException();
            }

            return new GithubOAuthToken(
                    response.access_token(),
                    response.token_type(),
                    response.scope()
            );
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().is4xxClientError()) {
                throw new GithubOAuthInvalidCodeException(ex);
            }
            throw new GithubOAuthTokenRequestFailedException(ex);
        } catch (WebClientRequestException ex) {
            throw new GithubOAuthTokenRequestFailedException(ex);
        }
    }

    @Override
    public GithubOAuthUser fetchUser(String accessToken) {
        try {
            GithubUserResponse response = githubApiWebClient.get()
                    .uri("/user")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(GithubUserResponse.class)
                    .block();

            if (response == null || response.login() == null || response.login().isBlank()) {
                throw new GithubOAuthUserFetchFailedException();
            }

            return new GithubOAuthUser(response.login(), response.node_id());
        } catch (WebClientResponseException | WebClientRequestException ex) {
            throw new GithubOAuthUserFetchFailedException(ex);
        }
    }

    @Override
    public List<String> fetchEmails(String accessToken) {
        try {
            GithubEmailResponse[] response = githubApiWebClient.get()
                    .uri("/user/emails")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(GithubEmailResponse[].class)
                    .block();

            if (response == null) {
                return Collections.emptyList();
            }

            return Arrays.stream(response)
                    .map(GithubEmailResponse::email)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .distinct()
                    .toList();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().value() == 403 || ex.getStatusCode().value() == 404) {
                return Collections.emptyList();
            }
            throw new GithubOAuthUserFetchFailedException(ex);
        } catch (WebClientRequestException ex) {
            throw new GithubOAuthUserFetchFailedException(ex);
        }
    }

    private record GithubAccessTokenResponse(
            String access_token,
            String token_type,
            String scope,
            String error,
            String error_description,
            String error_uri
    ) {
    }

    private record GithubUserResponse(
            String login,
            String node_id
    ) {
    }

    private record GithubEmailResponse(
            String email,
            boolean primary,
            boolean verified,
            String visibility
    ) {
    }
}
