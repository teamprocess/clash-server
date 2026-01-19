package com.process.clash.adapter.web.ranking.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.ranking.dto.GetRankingDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "랭킹 조회 응답")
public class GetRankingResponseDoc extends SuccessResponseDoc {

    @Schema(description = "랭킹 정보", implementation = GetRankingDto.Response.class)
    public GetRankingDto.Response data;
}
