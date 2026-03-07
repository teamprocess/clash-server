package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.battle.data.ApplyBattleData;
import com.process.clash.application.compete.rival.battle.policy.ApplyBattlePolicy;
import com.process.clash.application.compete.rival.battle.port.in.ApplyBattleUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyBattleService implements ApplyBattleUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserNoticeRepositoryPort userNoticeRepositoryPort;
    private final ApplyBattlePolicy applyBattlePolicy;
    private final CompeteRefetchNotifier competeRefetchNotifier;
    private final ZoneId battleZoneId;

    @Override
    public void execute(ApplyBattleData.Command command) {

        applyBattlePolicy.check(command.id());

        Long userId = command.actor().id();
        Long rivalEntityId = command.id();

        // 상대방 사용자 ID 조회
        Long opponentUserId = rivalRepositoryPort.findOpponentIdByIdAndUserId(rivalEntityId, userId);

        userNoticeRepositoryPort.deleteCancelBattleNoticeBySenderAndReceiver(userId, opponentUserId);

        // 배틀 생성 (Rival 엔티티 ID 사용)
        LocalDate startDate = LocalDate.now(battleZoneId);
        LocalDate endDate = startDate.plusDays(command.duration());

        Battle battle = Battle.createDefault(startDate, endDate, rivalEntityId, userId);
        Battle savedBattle = battleRepositoryPort.save(battle);

        // 상대방에게 알림 전송 (상대방 사용자 ID 사용)
        UserNotice userNoticeForOpponent = UserNotice.createForBattle(
                NoticeCategory.APPLY_BATTLE,
                userId,           // 신청자 (나)
                opponentUserId,   // 받는 사람 (상대방)
                savedBattle.id()  // 배틀 ID
        );

        userNoticeRepositoryPort.save(userNoticeForOpponent);
        competeRefetchNotifier.notifyUserNoticeChanged(List.of(opponentUserId));
        competeRefetchNotifier.notifyCompeteChanged(List.of(opponentUserId, userId));
    }
}
