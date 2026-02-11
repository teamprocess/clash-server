package com.process.clash.adapter.web.roadmap.missions.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.missions.dto.MissionSubmitDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 정답 제출 응답")
public class MissionSubmitResponseDocument extends SuccessResponseDocument {

    @Schema(description = "정답 제출 결과", implementation = MissionSubmitDto.Response.class)
    public MissionSubmitDto.Response data;
}
