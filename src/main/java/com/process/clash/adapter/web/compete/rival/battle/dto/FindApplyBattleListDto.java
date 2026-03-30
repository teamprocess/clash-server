package com.process.clash.adapter.web.compete.rival.battle.dto;

import com.process.clash.application.compete.rival.battle.data.FindApplyBattleListData;

import java.util.List;

public class FindApplyBattleListDto {

    public record Response(
            List<BattleApplyInfo> battles
    ) {
        public static Response from(FindApplyBattleListData.Result result) {
            return new Response(
                    result.battles().stream()
                            .map(info -> new BattleApplyInfo(
                                    info.id(),
                                    new Enemy(info.enemy().id(), info.enemy().name(), info.enemy().profileImage()),
                                    info.duration(),
                                    info.isMine()
                            ))
                            .toList()
            );
        }
    }

    private record BattleApplyInfo(
            Long id,
            Enemy enemy,
            int duration,
            Boolean isMine
    ) {}

    private record Enemy(
            Long id,
            String name,
            String profileImage
    ) {}
}