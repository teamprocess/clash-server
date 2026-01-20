package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.AnalyzeBattleInfoData;

public class AnalyzeBattleInfoDto {

    public record Response(
            String category,
            Long id,
            Integer enemyPoint,
            Integer myPoint
    ) {

        public static Response from(AnalyzeBattleInfoData.Result result) {

            return new Response(
                    result.category(),
                    result.id(),
                    result.enemyPoint(),
                    result.myPoint()
            );
        }
    }
}
