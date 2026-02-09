package com.process.clash.adapter.web.compete.rival.battle.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배틀 신청 요청")
public class ApplyBattleRequestDocument {

    @Schema(description = "라이벌 ID", example = "3")
    public Long id;

    @Schema(description = "배틀 기간 (일)", example = "7")
    public Integer duration;
}