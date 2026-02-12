package com.process.clash.application.user.usergithub.service;

import com.process.clash.application.user.usergithub.event.UserGitHubLinkedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGitHubLinkedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(Long userId) {
        if (userId == null) {
            return;
        }
        applicationEventPublisher.publishEvent(new UserGitHubLinkedEvent(userId));
    }
}
