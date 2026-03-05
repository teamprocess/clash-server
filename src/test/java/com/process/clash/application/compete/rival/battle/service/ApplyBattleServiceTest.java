package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import com.process.clash.application.compete.rival.battle.policy.ApplyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyBattleServiceTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Mock
    private ApplyBattlePolicy applyBattlePolicy;

    @Mock
    private CompeteRefetchNotifier competeRefetchNotifier;

    private ApplyBattleService applyBattleService;

    @BeforeEach
    void setUp() {
        applyBattleService = new ApplyBattleService(
            battleRepositoryPort,
            rivalRepositoryPort,
            userNoticeRepositoryPort,
            applyBattlePolicy,
            competeRefetchNotifier,
            ZoneId.of("Asia/Seoul")
        );
    }

    @Test
    @DisplayName("배틀 신청 시 알림 변경과 경쟁 데이터 변경 소켓 이벤트를 전송한다")
    void execute_notifiesNoticeAndCompeteChangeOnApply() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        ApplyBattleData.Command command = new ApplyBattleData.Command(actor, rivalId, 7);
        Battle savedBattle = new Battle(
            100L,
            Instant.now(),
            Instant.now(),
            LocalDate.now(),
            LocalDate.now().plusDays(7),
            com.process.clash.domain.rival.battle.enums.BattleStatus.PENDING,
            null,
            rivalId
        );

        when(rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalId, actor.id())).thenReturn(opponentId);
        when(battleRepositoryPort.save(any(Battle.class))).thenReturn(savedBattle);
        when(userNoticeRepositoryPort.save(any(UserNotice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        applyBattleService.execute(command);

        verify(competeRefetchNotifier).notifyUserNoticeChanged(java.util.List.of(opponentId));
        ArgumentCaptor<Collection<Long>> competeCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(competeRefetchNotifier).notifyCompeteChanged(competeCaptor.capture());
        assertThat(competeCaptor.getValue()).containsExactlyInAnyOrder(actor.id(), opponentId);
    }
}
