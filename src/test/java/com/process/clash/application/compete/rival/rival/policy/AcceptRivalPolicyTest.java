package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptRivalPolicyTest {

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    private AcceptRivalPolicy acceptRivalPolicy;

    @BeforeEach
    void setUp() {
        acceptRivalPolicy = new AcceptRivalPolicy(rivalRepositoryPort);
    }

    // ===== 내 ACCEPTED 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("내 ACCEPTED 라이벌 수가 4명이면 수락 시 예외가 발생한다")
    void check_throwsWhenMyAcceptedRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(4);

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    // ===== 상대방 ACCEPTED 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("상대방의 ACCEPTED 라이벌 수가 4명이면 수락 시 예외가 발생한다")
    void check_throwsWhenOpponentAcceptedRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(4);

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    // ===== 라이벌 미존재 케이스 =====

    @Test
    @DisplayName("라이벌 ID가 존재하지 않으면 예외가 발생한다")
    void check_throwsWhenRivalNotFound() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(2);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(1);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(RivalNotFoundException.class);
    }

    // ===== 정상 케이스 =====

    @Test
    @DisplayName("양측 모두 ACCEPTED 라이벌 수가 4명 미만이면 라이벌 엔티티를 반환한다")
    void check_returnsRivalWhenBothUnderLimit() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(2);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        Rival result = acceptRivalPolicy.check(actor, rivalId);

        assertThat(result).isEqualTo(rival);
    }

    @Test
    @DisplayName("ACCEPTED 3명 + 다수의 PENDING이 있어도 수락할 수 있다")
    void check_returnsRivalWhenAcceptedIsThreeRegardlessOfPending() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(3);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        Rival result = acceptRivalPolicy.check(actor, rivalId);

        assertThat(result).isEqualTo(rival);
    }

    @Test
    @DisplayName("양측 모두 라이벌이 없는 상태에서 수락하면 라이벌 엔티티를 반환한다")
    void check_returnsRivalWhenBothHaveNoRivals() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.countAcceptedByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countAcceptedByUserId(opponentId)).thenReturn(0);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        Rival result = acceptRivalPolicy.check(actor, rivalId);

        assertThat(result).isEqualTo(rival);
    }
}