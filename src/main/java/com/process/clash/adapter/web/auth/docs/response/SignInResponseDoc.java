package com.process.clash.adapter.web.auth.docs.response;

import com.process.clash.adapter.web.auth.dto.SignInDto;
import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 성공 응답")
public class SignInResponseDoc extends SuccessResponseDoc {

    @Schema(description = "로그인 결과", implementation = SignInDto.Response.class)
    public SignInDto.Response data;
}
