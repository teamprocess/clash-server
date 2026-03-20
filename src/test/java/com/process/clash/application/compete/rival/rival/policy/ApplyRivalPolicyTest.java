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

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    // ===== 내 ACCEPTED 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("내 ACCEPTED 라이벌 수가 4명이면 신청 시 예외가 발생한다")
    void check_throwsWhenMyAcceptedRivalCountIsMax() {
        Actor actor = new Actor(1L);
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(2L))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(4);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    // ===== 이미 관계 존재 케이스 =====

    @Test
    @DisplayName("이미 신청 중인 상대에게 다시 신청하면 예외가 발생한다")
    void check_throwsWhenAlreadyAppliedToOpponent() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    @Test
    @DisplayName("상대방이 나에게 PENDING 신청 중일 때 내가 신청하면 예외가 발생한다")
    void check_throwsWhenOpponentAlreadyAppliedToMe() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    @Test
    @DisplayName("이미 ACCEPTED 라이벌인 상대에게 신청하면 예외가 발생한다")
    void check_throwsWhenAlreadyAcceptedRival() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(1);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(true);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(AlreadyAppliedRivalException.class);
    }

    // ===== 상대방 ACCEPTED 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("상대방의 ACCEPTED 라이벌 수가 4명이면 예외가 발생한다")
    void check_throwsWhenOpponentAcceptedRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(false);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(4);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    @Test
    @DisplayName("여러 명 신청 시 한 명이라도 ACCEPTED 라이벌 수가 4명이면 예외가 발생한다")
    void check_throwsWhenAnyOpponentAcceptedRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long opponentId1 = 2L;
        Long opponentId2 = 3L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId1), new ApplyRivalData.Id(opponentId2))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId1)).thenReturn(false);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId2)).thenReturn(false);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId1)).thenReturn(2);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId2)).thenReturn(4);

        assertThatThrownBy(() -> applyRivalPolicy.check(command))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    // ===== 정상 케이스 =====

    @Test
    @DisplayName("모든 조건을 만족하면 라이벌 신청에 성공한다")
    void check_succeedsWhenAllConditionsAreMet() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(false);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(3);

        assertThatCode(() -> applyRivalPolicy.check(command)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ACCEPTED 3명 + 다수의 PENDING이 있어도 신청할 수 있다")
    void check_succeedsWhenMyAcceptedIsThreeRegardlessOfPending() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        // ACCEPTED 3명 (PENDING은 카운트하지 않음)
        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(false);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(0);

        assertThatCode(() -> applyRivalPolicy.check(command)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("활성 라이벌이 없는 상태에서 신청하면 성공한다")
    void check_succeedsWhenBothHaveNoRivals() {
        Actor actor = new Actor(1L);
        Long opponentId = 2L;
        ApplyRivalData.Command command = new ApplyRivalData.Command(
                actor,
                List.of(new ApplyRivalData.Id(opponentId))
        );

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.existsActiveRivalBetween(actor.id(), opponentId)).thenReturn(false);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(0);

        assertThatCode(() -> applyRivalPolicy.check(command)).doesNotThrowAnyException();
    }
}