package com.process.clash.application.user.user.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WithdrawServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private BattleRepositoryPort battleRepositoryPort;

    @Mock
    private SessionManager sessionManager;

    @InjectMocks
    private WithdrawService withdrawService;

    @Test
    @DisplayName("탈퇴 시 해당 유저의 진행 중인 배틀이 REJECTED 처리된다")
    void execute_rejectsAllActiveBattles() {
        Actor actor = new Actor(1L);

        withdrawService.execute(actor);

        verify(battleRepositoryPort).rejectAllActiveBattlesByUserId(1L);
    }

    @Test
    @DisplayName("탈퇴 시 해당 유저가 속한 모든 라이벌 관계가 삭제된다")
    void execute_deletesAllRivals() {
        Actor actor = new Actor(1L);

        withdrawService.execute(actor);

        verify(rivalRepositoryPort).deleteAllByUserId(1L);
    }

    @Test
    @DisplayName("탈퇴 시 유저 데이터가 soft delete된다")
    void execute_softDeletesUser() {
        Actor actor = new Actor(1L);

        withdrawService.execute(actor);

        verify(userRepositoryPort).deleteById(1L);
    }

    @Test
    @DisplayName("탈퇴 시 세션이 만료된다")
    void execute_invalidatesSession() {
        Actor actor = new Actor(1L);

        withdrawService.execute(actor);

        verify(sessionManager).invalidateSession();
    }

    @Test
    @DisplayName("탈퇴 처리는 배틀 REJECTED → 라이벌 삭제 → 유저 삭제 → 세션 만료 순으로 실행된다")
    void execute_runsInCorrectOrder() {
        Actor actor = new Actor(1L);

        withdrawService.execute(actor);

        InOrder inOrder = inOrder(battleRepositoryPort, rivalRepositoryPort, userRepositoryPort, sessionManager);
        inOrder.verify(battleRepositoryPort).rejectAllActiveBattlesByUserId(1L);
        inOrder.verify(rivalRepositoryPort).deleteAllByUserId(1L);
        inOrder.verify(userRepositoryPort).deleteById(1L);
        inOrder.verify(sessionManager).invalidateSession();
    }
}
