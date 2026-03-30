package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.user.entity.User;

import java.util.List;

public class FindApplyBattleListData {

    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
            List<BattleApplyInfo> battles
    ) {
        public static Result from(List<BattleApplyInfo> battles) {
            return new Result(battles);
        }
    }

    public record BattleApplyInfo(
            Long id,
            Enemy enemy,
            int duration,
            Boolean isMine
    ) {
        public static BattleApplyInfo of(Long id, Enemy enemy, int duration, Boolean isMine) {
            return new BattleApplyInfo(id, enemy, duration, isMine);
        }
    }

    public record Enemy(
            Long id,
            String name,
            String profileImage
    ) {
        public static Enemy from(User user) {
            return new Enemy(user.id(), user.name(), user.profileImage());
        }
    }
}