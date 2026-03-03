package com.process.clash.application.user.user.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.in.WithdrawUseCase;
import com.process.clash.application.user.user.port.out.SessionManager;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawService implements WithdrawUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final BattleRepositoryPort battleRepositoryPort;
    private final SessionManager sessionManager;

    @Override
    public void execute(Actor actor) {
        battleRepositoryPort.rejectAllActiveBattlesByUserId(actor.id());
        rivalRepositoryPort.deleteAllByUserId(actor.id());
        userRepositoryPort.deleteById(actor.id());
        sessionManager.invalidateSession();
    }
}
