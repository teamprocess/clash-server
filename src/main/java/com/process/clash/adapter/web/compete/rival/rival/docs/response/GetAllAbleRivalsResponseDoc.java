package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.rival.rival.dto.GetAllAbleRivalsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "라이벌 등록 가능 유저 목록 응답")
public class GetAllAbleRivalsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "유저 목록", implementation = GetAllAbleRivalsDto.Response.class)
    public GetAllAbleRivalsDto.Response data;
}
