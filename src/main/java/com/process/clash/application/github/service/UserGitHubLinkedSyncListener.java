package com.process.clash.application.github.service;

import com.process.clash.application.github.port.in.SyncGithubDailyStatsUseCase;
import com.process.clash.application.user.usergithub.event.UserGitHubLinkedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserGitHubLinkedSyncListener {

    private final SyncGithubDailyStatsUseCase syncGithubDailyStatsUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onUserGitHubLinked(UserGitHubLinkedEvent event) {
        if (event == null || event.userId() == null) {
            return;
        }
        syncGithubDailyStatsUseCase.syncRecent365DaysForUser(event.userId());
    }
}
