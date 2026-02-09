package com.process.clash.adapter.web.roadmap.missions.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.missions.dto.MissionResultDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 결과 응답")
public class GetMissionResultResponseDocument extends SuccessResponseDocument {

    @Schema(description = "미션 결과", implementation = MissionResultDto.Response.class)
    public MissionResultDto.Response data;
}
