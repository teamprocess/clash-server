package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.policy.ModifyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.in.AcceptBattleUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcceptBattleService implements AcceptBattleUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final ModifyBattlePolicy modifyBattlePolicy;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public void execute(ModifyBattleData.Command command) {

        modifyBattlePolicy.check(command.id());

        Battle battle = battleRepositoryPort.findById(command.id())
                .orElseThrow(BattleNotFoundException::new);

        Battle updatedBattle = battle.accept();

        battleRepositoryPort.save(updatedBattle);

        UserNotice userNoticeForReceiver = UserNotice
                .createDefault(
                        NoticeCategory.ACCEPT_BATTLE,
                        command.actor().id(),
                        rivalRepositoryPort.findOpponentIdByIdAndUserId(updatedBattle.rivalId(), command.actor().id())
                );

        userNoticeRepositoryPort.save(userNoticeForReceiver);

        UserNotice userNoticeForSender = UserNotice
                .createDefault(
                        NoticeCategory.SHOW_ACCEPT_BATTLE,
                        command.actor().id(),
                        command.actor().id()
                );

        userNoticeRepositoryPort.save(userNoticeForSender);
    }
}
