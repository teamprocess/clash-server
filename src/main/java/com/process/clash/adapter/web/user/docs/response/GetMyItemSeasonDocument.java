package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시즌 정보")
public class GetMyItemSeasonDocument {

    @Schema(description = "시즌 ID", example = "1")
    public Long id;

    @Schema(description = "시즌 이름", example = "Season 1")
    public String name;

    @Schema(description = "시즌 시작일", example = "2025-01-01")
    public String startDate;

    @Schema(description = "시즌 종료일", example = "2025-03-31")
    public String endDate;
}
