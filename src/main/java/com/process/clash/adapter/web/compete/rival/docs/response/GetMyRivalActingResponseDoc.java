package com.process.clash.adapter.web.compete.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.rival.dto.GetMyRivalActingDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 라이벌 상태 조회 응답")
public class GetMyRivalActingResponseDoc extends SuccessResponseDoc {

    @Schema(description = "라이벌 정보", implementation = GetMyRivalActingDto.Response.class)
    public GetMyRivalActingDto.Response data;
}
