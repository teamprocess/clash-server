package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.FindAbleRivalsData;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FindAbleRivalsDto {

    @Schema(name = "FindAbleRivalsDtoResponse")
    public record Response(
            List<AbleRivalInfoForBattle> rivals
    ) {

        public static Response from(FindAbleRivalsData.Result result) {

            return new Response(
                    result.rivals()
            );
        }
    }
}