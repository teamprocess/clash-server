package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.NotAcceptedBattleException;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.battle.service.BattleFinishService;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class GetBattleInfoPolicy {

    private final BattleRepositoryPort battleRepositoryPort;
    private final BattleFinishService battleFinishService;

    public Battle check(Long id) {

        Battle battle = battleRepositoryPort.findById(id)
                .orElseThrow(BattleNotFoundException::new);

        // 스케줄러 미실행 구간에도 조회 시점에 DONE 처리 및 DB 저장
        battle = battleFinishService.finishSingleIfExpired(battle);

        if (battle.battleStatus().equals(BattleStatus.PENDING) || battle.battleStatus().equals(BattleStatus.REJECTED)) {
            throw new NotAcceptedBattleException();
        }

        return battle;
    }
}
