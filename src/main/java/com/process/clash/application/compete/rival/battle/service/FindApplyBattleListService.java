package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindApplyBattleListData;
import com.process.clash.application.compete.rival.battle.port.in.FindApplyBattleListUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindApplyBattleListService implements FindApplyBattleListUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public FindApplyBattleListData.Result execute(FindApplyBattleListData.Command command) {
        Long userId = command.actor().id();

        List<Battle> pendingBattles = battleRepositoryPort.findPendingBattlesByUserId(userId);

        if (pendingBattles.isEmpty()) {
            return FindApplyBattleListData.Result.from(List.of());
        }

        Set<Long> rivalIds = pendingBattles.stream()
                .map(Battle::rivalId)
                .collect(Collectors.toSet());

        Map<Long, Rival> rivalMap = rivalRepositoryPort.findByIdIn(rivalIds).stream()
                .collect(Collectors.toMap(Rival::id, rival -> rival));

        Set<Long> enemyIds = pendingBattles.stream()
                .map(battle -> {
                    Rival rival = rivalMap.get(battle.rivalId());
                    return rival.firstUserId().equals(userId)
                            ? rival.secondUserId()
                            : rival.firstUserId();
                })
                .collect(Collectors.toSet());

        Map<Long, User> userMap = userRepositoryPort.findByIdIn(enemyIds).stream()
                .collect(Collectors.toMap(User::id, user -> user));

        List<FindApplyBattleListData.BattleApplyInfo> battleApplyInfos = pendingBattles.stream()
                .map(battle -> {
                    Rival rival = rivalMap.get(battle.rivalId());
                    Long enemyId = rival.firstUserId().equals(userId)
                            ? rival.secondUserId()
                            : rival.firstUserId();
                    User enemy = userMap.get(enemyId);
                    boolean isMine = userId.equals(battle.applicantId());
                    return FindApplyBattleListData.BattleApplyInfo.of(
                            battle.id(),
                            FindApplyBattleListData.Enemy.from(enemy),
                            battle.duration(),
                            isMine
                    );
                })
                .toList();

        return FindApplyBattleListData.Result.from(battleApplyInfos);
    }
}