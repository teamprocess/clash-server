package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.Enemy;
import com.process.clash.application.compete.rival.battle.data.FindDetailedBattleInfoData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class FindDetailedBattleInfoDto {

    @Schema(name = "FindDetailedBattleInfoDtoResponse")
    public record Response(
            Long id,
            Enemy enemy,
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