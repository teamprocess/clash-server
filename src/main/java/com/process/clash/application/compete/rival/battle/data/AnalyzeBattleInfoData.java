package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;

public class AnalyzeBattleInfoData {

    public record Command(
            Actor actor,
            Long id,
            TargetCategory category
    ) {

        public static Command of(Actor actor, Long id, TargetCategory category) {

            return new Command(
                    actor,
                    id,
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

        public static Result of(TargetCategory category, Long id, Integer enemyPoint, Integer myPoint) {

            return new Result(
                    category.toString(),
                    id,
                    enemyPoint,
                    myPoint
            );
        }
    }
}
