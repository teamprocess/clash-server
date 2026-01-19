package com.process.clash.adapter.web.auth.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증")
public class VerifyEmailDoc {

    @Schema(description = "이메일")
    public String email;

    @Schema(description = "비밀번호")
    public String code;
}
