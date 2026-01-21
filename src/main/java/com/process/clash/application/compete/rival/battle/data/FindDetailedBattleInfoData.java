package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.user.user.entity.User;

import java.time.LocalDate;

public class FindDetailedBattleInfoData {

    public record Command(
            Actor actor,
            Long id
    ) {

        public static Command from(Actor actor, Long id) {

            return new Command(
                    actor,
                    id
            );
        }
    }

    public record Result(
            Long id,
            Enemy enemy,
            LocalDate expireDate,
            Double myOverallPercentage
    ) {

        public static Result from(Battle battle, User user, Double myOverallPercentage) {

            return new Result(
                    battle.id(),
                    Enemy.from(user),
                    battle.endDate(),
                    myOverallPercentage
            );
        }
    }
}
