package com.process.clash.application.github.service;

import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.application.github.port.out.GithubDailyStatsStorePort;
import com.process.clash.application.github.port.out.GithubStatsFetchPort;
import com.process.clash.application.github.port.out.GithubSyncTargetPort;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubDailyStatsSyncServiceTest {

    @Mock
    private GithubSyncTargetPort syncTargetPort;

    @Mock
    private GithubStatsFetchPort statsFetchPort;

    @Mock
    private GithubDailyStatsStorePort statsStorePort;

    private StudyDateCalculator studyDateCalculator;
    private Clock clock;
    private ExecutorService executor;

    private GithubDailyStatsSyncService syncService;

    @Captor
    private ArgumentCaptor<GithubSyncTarget> targetCaptor;

    @BeforeEach
    void setUp() {
        // 테스트 기준 시간을 고정해 날짜 계산 결과를 안정화
        ZoneId zoneId = ZoneId.of("UTC");
        studyDateCalculator = new StudyDateCalculator(zoneId, 6);
        clock = Clock.fixed(Instant.parse("2026-01-20T10:00:00Z"), zoneId);
        executor = Executors.newFixedThreadPool(2);

        // 스프링 컨텍스트 없이 직접 생성해 단위 테스트로 검증
        syncService = new GithubDailyStatsSyncService(
                syncTargetPort,
                statsFetchPort,
                statsStorePort,
                studyDateCalculator,
                clock,
                executor
        );
        ReflectionTestUtils.setField(syncService, "recomputeDays", 1);
        ReflectionTestUtils.setField(syncService, "defaultToken", "");
        ReflectionTestUtils.setField(syncService, "maxConcurrency", 1);
    }

    @AfterEach
    void tearDown() {
        // 병렬 실행에 사용한 스레드풀 정리
        executor.shutdown();
    }

    @Test
    @DisplayName("동기화 대상이 없으면 외부 호출을 하지 않는다")
    void syncRecentDays_skipsWhenNoTargets() {
        when(syncTargetPort.findSyncTargets()).thenReturn(List.of());

        syncService.syncRecentDays();

        verifyNoInteractions(statsFetchPort, statsStorePort);
    }

    @Test
    @DisplayName("기본 토큰이 있으면 사용자 토큰이 없어도 사용한다")
    void syncRecentDays_usesDefaultTokenWhenMissing() {
        GithubSyncTarget target = new GithubSyncTarget(1L, "login", "nodeId", List.of(), null);
        when(syncTargetPort.findSyncTargets()).thenReturn(List.of(target));
        when(statsFetchPort.fetchDailyStats(any(), any())).thenReturn(List.of());

        // 사용자 토큰이 없을 때 기본 토큰이 주입되는지 검증
        ReflectionTestUtils.setField(syncService, "defaultToken", "default-token");

        syncService.syncRecentDays();

        verify(statsFetchPort).fetchDailyStats(targetCaptor.capture(), any());
        assertThat(targetCaptor.getValue().accessToken()).isEqualTo("default-token");
        verifyNoInteractions(statsStorePort);
    }

    @Test
    @DisplayName("토큰이 없으면 동기화를 건너뛴다")
    void syncRecentDays_skipsWhenTokenMissing() {
        GithubSyncTarget target = new GithubSyncTarget(1L, "login", "nodeId", List.of(), null);
        when(syncTargetPort.findSyncTargets()).thenReturn(List.of(target));

        syncService.syncRecentDays();

        verifyNoInteractions(statsFetchPort, statsStorePort);
    }

    @Test
    @DisplayName("통계가 있으면 저장을 수행한다")
    void syncRecentDays_persistsWhenStatsPresent() {
        GithubSyncTarget target = new GithubSyncTarget(1L, "login", "nodeId", List.of(), "token");
        when(syncTargetPort.findSyncTargets()).thenReturn(List.of(target));

        // 조회 결과가 존재하면 저장 포트가 호출되어야 함
        LocalDate studyDate = studyDateCalculator.toStudyDate(clock.instant());
        GitHubDailyStats stats = new GitHubDailyStats(
                1L,
                studyDate,
                1,
                2,
                3,
                4,
                5L,
                6L,
                clock.instant()
        );
        when(statsFetchPort.fetchDailyStats(any(), any())).thenReturn(List.of(stats));

        syncService.syncRecentDays();

        verify(statsStorePort).upsertAll(List.of(stats));
    }

    @Test
    @DisplayName("병렬 처리 설정 시 모든 대상이 처리된다")
    void syncRecentDays_runsInParallelWhenConfigured() {
        GithubSyncTarget t1 = new GithubSyncTarget(1L, "login1", "node1", List.of(), "token1");
        GithubSyncTarget t2 = new GithubSyncTarget(2L, "login2", "node2", List.of(), "token2");
        when(syncTargetPort.findSyncTargets()).thenReturn(List.of(t1, t2));
        when(statsFetchPort.fetchDailyStats(any(), any())).thenReturn(List.of());

        ReflectionTestUtils.setField(syncService, "maxConcurrency", 2);

        syncService.syncRecentDays();

        verify(statsFetchPort, times(2)).fetchDailyStats(any(), any());
        verifyNoInteractions(statsStorePort);
    }
}
