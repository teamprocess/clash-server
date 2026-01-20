package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.FindAbleRivalsData;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;

import java.util.List;

public class FindAbleRivalsDto {

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
