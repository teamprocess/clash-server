package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;

public class AnalyzeBattleInfoData {

    public record Command(
            Actor actor,
            TargetCategory category
    ) {

        public static Command from(Actor actor, TargetCategory category) {

            return new Command(
                    actor,
                    category
            );
        }
    }

    public record Result(
            String category,
            Long id,
            Integer enemyPoint,
            Integer myPoint
    ) {

        public static Result from(TargetCategory category, Long id, Integer enemyPoint, Integer myPoint) {

            return new Result(
                    category.toString(),
                    id,
                    enemyPoint,
                    myPoint
            );
        }
    }
}
