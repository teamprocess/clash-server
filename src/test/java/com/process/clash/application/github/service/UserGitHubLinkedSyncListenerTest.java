package com.process.clash.application.github.service;

import com.process.clash.application.github.port.in.SyncGithubDailyStatsUseCase;
import com.process.clash.application.user.usergithub.event.UserGitHubLinkedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserGitHubLinkedSyncListenerTest {

    @Mock
    private SyncGithubDailyStatsUseCase syncGithubDailyStatsUseCase;

    @Test
    @DisplayName("연동 이벤트를 받으면 해당 유저 365일 동기화를 호출한다")
    void onUserGitHubLinked_callsUser365DaysSync() {
        UserGitHubLinkedSyncListener listener = new UserGitHubLinkedSyncListener(syncGithubDailyStatsUseCase);

        listener.onUserGitHubLinked(new UserGitHubLinkedEvent(1L));

        verify(syncGithubDailyStatsUseCase).syncRecent365DaysForUser(1L);
    }

    @Test
    @DisplayName("이벤트 userId가 없으면 동기화를 호출하지 않는다")
    void onUserGitHubLinked_skipsWhenUserIdMissing() {
        UserGitHubLinkedSyncListener listener = new UserGitHubLinkedSyncListener(syncGithubDailyStatsUseCase);

        listener.onUserGitHubLinked(new UserGitHubLinkedEvent(null));

        verifyNoInteractions(syncGithubDailyStatsUseCase);
    }
}
