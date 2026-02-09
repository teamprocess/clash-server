package com.process.clash.adapter.web.mainpage.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메인 페이지 유저 정보 응답")
public class GetUserProfileResponseDocument extends SuccessResponseDocument {

    @Schema(description = "유저 프로필", implementation = GetUserProfileDto.Response.class)
    public GetUserProfileDto.Response data;
}
