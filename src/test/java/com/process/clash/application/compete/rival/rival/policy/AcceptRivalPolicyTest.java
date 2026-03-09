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

    // ===== 내 활성 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("내 활성 라이벌 수가 4명이면 수락 시 예외가 발생한다")
    void check_throwsWhenMyActiveRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(4);

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    @Test
    @DisplayName("ACCEPTED 3명 + 내가 신청한 PENDING 1명 = 4명일 때 수락하면 예외가 발생한다")
    void check_throwsWhenAcceptedAndPendingSentSumIsMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;

        // ACCEPTED 3 + 내가 신청한 PENDING 1 = 합산 4
        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(4);

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    // ===== 상대방 활성 라이벌 수 초과 케이스 =====

    @Test
    @DisplayName("상대방의 활성 라이벌 수가 4명이면 수락 시 예외가 발생한다")
    void check_throwsWhenOpponentActiveRivalCountIsMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countActiveByUserId(opponentId)).thenReturn(4);

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(TooMuchRivalsException.class);
    }

    @Test
    @DisplayName("상대방의 ACCEPTED + 신청한 PENDING 합산이 4명이면 수락 시 예외가 발생한다")
    void check_throwsWhenOpponentActiveCountReachesMax() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        // 상대방의 ACCEPTED 3 + 신청한 PENDING 1 = 합산 4
        when(rivalRepositoryPort.countActiveByUserId(opponentId)).thenReturn(4);

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

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(2);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countActiveByUserId(opponentId)).thenReturn(1);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> acceptRivalPolicy.check(actor, rivalId))
                .isInstanceOf(RivalNotFoundException.class);
    }

    // ===== 정상 케이스 =====

    @Test
    @DisplayName("양측 모두 활성 라이벌 수가 4명 미만이면 라이벌 엔티티를 반환한다")
    void check_returnsRivalWhenBothUnderLimit() {
        Actor actor = new Actor(1L);
        Long rivalId = 10L;
        Long opponentId = 2L;
        Rival rival = new Rival(rivalId, Instant.now(), Instant.now(), RivalLinkingStatus.PENDING, opponentId, actor.id());

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(3);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countActiveByUserId(opponentId)).thenReturn(2);
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

        when(rivalRepositoryPort.countActiveByUserId(actor.id())).thenReturn(0);
        when(rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(rivalId, actor.id())).thenReturn(opponentId);
        when(rivalRepositoryPort.countActiveByUserId(opponentId)).thenReturn(0);
        when(rivalRepositoryPort.findById(rivalId)).thenReturn(Optional.of(rival));

        Rival result = acceptRivalPolicy.check(actor, rivalId);

        assertThat(result).isEqualTo(rival);
    }
}
