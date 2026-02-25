package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ModifyBattleData;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.in.RejectBattleUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectBattleService implements RejectBattleUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void execute(ModifyBattleData.Command command) {

        Battle battle = battleRepositoryPort.findById(command.id())
                .orElseThrow(BattleNotFoundException::new);

        Battle updatedBattle = battle.reject();

        Battle savedBattle = battleRepositoryPort.save(updatedBattle);
        Long opponentId = rivalRepositoryPort.findOpponentIdByIdAndUserId(savedBattle.rivalId(), command.actor().id());

        UserNotice userNoticeForReceiver = UserNotice
                .createDefault(
                        NoticeCategory.REJECT_BATTLE,
                        command.actor().id(),
                        opponentId
                );

        userNoticeRepositoryPort.save(userNoticeForReceiver);

        UserNotice userNoticeForSender = UserNotice
                .createDefault(
                        NoticeCategory.SHOW_REJECT_BATTLE,
                        command.actor().id(),
                        command.actor().id()
                );

        userNoticeRepositoryPort.save(userNoticeForSender);
        competeRefetchNotifier.notifyUserNoticeChanged(List.of(opponentId, command.actor().id()));
        competeRefetchNotifier.notifyCompeteChanged(List.of(opponentId, command.actor().id()));
    }
}
