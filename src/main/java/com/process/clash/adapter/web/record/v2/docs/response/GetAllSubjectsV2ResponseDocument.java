package com.process.clash.adapter.web.record.v2.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetAllSubjectsV2Dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "V2 과목 그룹 목록 조회 응답")
public class GetAllSubjectsV2ResponseDocument extends SuccessResponseDocument {

    @Schema(description = "과목 그룹 목록", implementation = GetAllSubjectsV2Dto.Response.class)
    public GetAllSubjectsV2Dto.Response data;
}
