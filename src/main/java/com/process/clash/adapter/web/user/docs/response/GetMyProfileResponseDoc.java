package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 계정/프로필 조회 응답")
public class GetMyProfileResponseDoc extends SuccessResponseDoc {

    @Schema(description = "내 계정/프로필", implementation = GetMyProfileDto.Response.class)
    public GetMyProfileDto.Response data;
}
