package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import com.process.clash.application.compete.rival.battle.policy.ApplyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.in.ApplyBattleUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyBattleService implements ApplyBattleUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final ApplyBattlePolicy applyBattlePolicy;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public void execute(ApplyBattleData.Command command) {

        applyBattlePolicy.check(command.id());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(command.duration());

        Battle battle = Battle.createDefault(startDate, endDate, command.id());
        battleRepositoryPort.save(battle);

        UserNotice userNotice = UserNotice
                .createDefault(
                        NoticeCategory.APPLY_BATTLE,
                        command.actor().id(),
                        rivalRepositoryPort.findOpponentIdByIdAndUserId(command.id(), command.actor().id())
                );

        userNoticeRepositoryPort.save(userNotice);
    }
}
