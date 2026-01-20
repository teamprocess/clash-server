package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.FindDetailedBattleInfoData;

import java.time.LocalDate;

public class FindDetailedBattleInfoDto {

    public record Response(
            Long id,
            FindDetailedBattleInfoData.Enemy enemy,
            LocalDate expireDate,
            Double myOverallPercentage
    ) {

        public static Response from(FindDetailedBattleInfoData.Result result) {

            return new Response(
                    result.id(),
                    result.enemy(),
                    result.expireDate(),
                    result.myOverallPercentage()
            );
        }
    }
}
