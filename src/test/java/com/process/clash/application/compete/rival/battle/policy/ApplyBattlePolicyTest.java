package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.conflict.AlreadyAppliedBattleException;
import com.process.clash.application.compete.rival.battle.exception.exception.conflict.AlreadyInBattleException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplyBattlePolicyTest {

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    private ApplyBattlePolicy applyBattlePolicy;

    @BeforeEach
    void setUp() {
        applyBattlePolicy = new ApplyBattlePolicy(battleRepositoryPort);
    }

    @Test
    @DisplayName("PENDING 상태의 배틀이 존재하면 AlreadyAppliedBattleException이 발생한다")
    void check_throwsAlreadyAppliedBattleException_whenPendingBattleExists() {
        Long rivalId = 1L;
        when(battleRepositoryPort.existsPendingBattleByRivalId(rivalId)).thenReturn(true);

        assertThatThrownBy(() -> applyBattlePolicy.check(rivalId))
            .isInstanceOf(AlreadyAppliedBattleException.class);
    }

    @Test
    @DisplayName("진행 중인 배틀이 존재하면 AlreadyInBattleException이 발생한다")
    void check_throwsAlreadyInBattleException_whenActiveBattleExists() {
        Long rivalId = 1L;
        when(battleRepositoryPort.existsPendingBattleByRivalId(rivalId)).thenReturn(false);
        when(battleRepositoryPort.existsActiveBattleByRivalId(rivalId)).thenReturn(true);

        assertThatThrownBy(() -> applyBattlePolicy.check(rivalId))
            .isInstanceOf(AlreadyInBattleException.class);
    }

    @Test
    @DisplayName("활성 또는 PENDING 상태의 배틀이 없으면 예외가 발생하지 않는다")
    void check_doesNotThrow_whenNoActiveOrPendingBattleExists() {
        Long rivalId = 1L;
        when(battleRepositoryPort.existsPendingBattleByRivalId(rivalId)).thenReturn(false);
        when(battleRepositoryPort.existsActiveBattleByRivalId(rivalId)).thenReturn(false);

        assertThatCode(() -> applyBattlePolicy.check(rivalId))
            .doesNotThrowAnyException();
    }
}