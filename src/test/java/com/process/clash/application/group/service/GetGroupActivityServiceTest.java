package com.process.clash.application.group.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.util.RecordDayWindow;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import com.process.clash.infrastructure.config.record.RecordProperties;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetGroupActivityServiceTest {

    @Mock
    private GroupRepositoryPort groupRepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionRepositoryPort;

    private GetGroupActivityService getGroupActivityService;

    @BeforeEach
    void setUp() {
        getGroupActivityService = new GetGroupActivityService(
            groupRepositoryPort,
            recordSessionRepositoryPort,
            new GroupPolicy(),
            new RecordProperties("UTC", 6),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("오늘 기록일이 아니면 해당 날짜 기준 공부 시간만 조회하고 실시간 활성 상태는 비운다")
    void execute_returnsDateBasedStudyTimesWithoutRealtimeStatus() {
        Long groupId = 10L;
        Actor actor = new Actor(1L);
        LocalDate requestedDate = RecordDayWindow.today(ZoneId.of("UTC"), 6).recordDate().minusDays(1);
        LocalDateTime dayStart = requestedDate.atTime(6, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        User member = createUser(2L, "조상철");

        when(groupRepositoryPort.existsById(groupId)).thenReturn(true);
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(true);
        when(groupRepositoryPort.findMembersByGroupId(groupId))
            .thenReturn(List.of(member));
        when(recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(List.of(2L), dayStart, dayEnd))
            .thenReturn(Map.of(2L, 3600L));

        GetGroupActivityData.Result result = getGroupActivityService.execute(
            GetGroupActivityData.Command.of(actor, groupId, requestedDate)
        );

        assertThat(result.members()).hasSize(1);
        assertThat(result.members().get(0).studyTime()).isEqualTo(3600L);
        assertThat(result.members().get(0).isStudying()).isFalse();
        verify(recordSessionRepositoryPort, never()).findAllActiveSessionsByUserIds(List.of(2L));
    }

    @Test
    @DisplayName("오늘 기록일이면 그룹원의 실시간 활성 상태를 함께 조회한다")
    void execute_includesRealtimeStatusForTodayRecordDate() {
        Long groupId = 10L;
        Actor actor = new Actor(1L);
        LocalDate todayRecordDate = RecordDayWindow.today(ZoneId.of("UTC"), 6).recordDate();
        LocalDateTime dayStart = todayRecordDate.atTime(6, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        User member = createUser(2L, "조상철");

        when(groupRepositoryPort.existsById(groupId)).thenReturn(true);
        when(groupRepositoryPort.existsMember(groupId, actor.id())).thenReturn(true);
        when(groupRepositoryPort.findMembersByGroupId(groupId))
            .thenReturn(List.of(member));
        when(recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(List.of(2L), dayStart, dayEnd))
            .thenReturn(Map.of(2L, 1200L));
        when(recordSessionRepositoryPort.findAllActiveSessionsByUserIds(List.of(2L)))
            .thenReturn(List.of(createActiveTaskSession(2L)));

        GetGroupActivityData.Result result = getGroupActivityService.execute(
            GetGroupActivityData.Command.of(actor, groupId, todayRecordDate)
        );

        assertThat(result.members()).hasSize(1);
        assertThat(result.members().get(0).studyTime()).isEqualTo(1200L);
        assertThat(result.members().get(0).isStudying()).isTrue();
    }

    private User createUser(Long id, String name) {
        return new User(
            id,
            Instant.now(),
            Instant.now(),
            "username-" + id,
            "user" + id + "@example.com",
            name,
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null,
            false,
            RankTier.NONE,
            ExpTier.UNRANKED
        );
    }

    private RecordSessionV2 createActiveTaskSession(Long userId) {
        return new RecordSessionV2(
            100L,
            userId,
            RecordSessionTypeV2.TASK,
            10L,
            "백엔드",
            11L,
            "ERD",
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(300),
            null
        );
    }
}
