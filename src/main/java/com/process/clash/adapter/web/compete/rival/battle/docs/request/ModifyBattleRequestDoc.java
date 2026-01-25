package com.process.clash.adapter.web.compete.rival.battle.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배틀 승인/거절 요청")
public class ModifyBattleRequestDoc {

    @Schema(description = "배틀 ID", example = "1")
    public Long id;
}