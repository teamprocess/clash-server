package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.rival.exception.exception.conflict.AlreadyAppliedRivalException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyRivalPolicyTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    private ApplyRivalPolicy applyRivalPolicy;

    @BeforeEach
    void setUp() {
        applyRivalPolicy = new ApplyRivalPolicy(rivalRepositoryPort);
    }

    @Test
    @DisplayName("이미 신청한 상대에게 다시 신청하면 예외가 발생한다")
    void check_throwsWhenAlreadyAppliedToOpponent() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAllByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    @Test
    @DisplayName("상대방이 나에게 신청해 둔 경우 내가 신청하면 예외가 발생한다")
    void check_throwsWhenOpponentAlreadyAppliedToMe() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAllByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    @Test
    @DisplayName("이미 라이벌인 상대에게 신청하면 예외가 발생한다")
    void check_throwsWhenAlreadyRivals() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAllByUserId(actor.id())).thenReturn(1);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    @Test
    @DisplayName("활성 라이벌 관계가 없는 상대에게는 신청할 수 있다")
    void check_succeedsWhenNoActiveRivalExists() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAllByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(false);
        when(rivalRepositoryPort.countAllByOpponentIdsGrouped(List.of(opponentId))).thenReturn(List.of());

        assertThatCode(() -> applyRivalPolicy.check(command)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("라이벌 최대 수 초과 시 예외가 발생한다")
    void check_throwsWhenExceedMaxRivalCount() {
        Actor actor = new Actor(1L);
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(2L))
        );

        when(rivalRepositoryPort.countAllByUserId(actor.id())).thenReturn(4);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(TooMuchRivalsException.class);
    }
}
