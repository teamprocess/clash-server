package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.FindAllBattleInfoData;

import java.util.List;

public class FindAllBattleInfoDto {

    public record Response(
            List<FindAllBattleInfoData.BattleInfo> battles
    ) {

        public static Response from(FindAllBattleInfoData.Result results) {

            return new Response(
                    results.battles()
            );
        }
    }
}
