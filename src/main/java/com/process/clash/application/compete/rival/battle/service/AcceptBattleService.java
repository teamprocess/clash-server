package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
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

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AcceptBattleService implements AcceptBattleUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final ModifyBattlePolicy modifyBattlePolicy;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;
    private final ZoneId battleZoneId;

    @Override
    public void execute(ModifyBattleData.Command command) {

        modifyBattlePolicy.check(command.id());

        Battle battle = battleRepositoryPort.findById(command.id())
                .orElseThrow(BattleNotFoundException::new);

        // 승인 시각을 배틀 시작 시각으로 사용, 종료 시각은 수락일(KST) + duration일의 자정 KST
        Battle updatedBattle = battle.accept(Instant.now(), battleZoneId);

        battleRepositoryPort.save(updatedBattle);

        userNoticeRepositoryPort.deleteApplyBattleNoticeByBattleId(command.id());

        Long opponentId = rivalRepositoryPort.findOpponentIdByIdAndUserId(updatedBattle.rivalId(), command.actor().id());

        UserNotice userNoticeForReceiver = UserNotice
                .createDefault(
                        NoticeCategory.ACCEPT_BATTLE,
                        command.actor().id(),
                        opponentId
                );

        userNoticeRepositoryPort.save(userNoticeForReceiver);

        List<Long> userIdsToNotify = List.of(opponentId, command.actor().id());
        competeRefetchNotifier.notifyUserNoticeChanged(userIdsToNotify);
        competeRefetchNotifier.notifyCompeteChanged(userIdsToNotify);
    }
}