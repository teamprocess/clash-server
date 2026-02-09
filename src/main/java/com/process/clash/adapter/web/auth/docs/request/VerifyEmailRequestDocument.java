package com.process.clash.adapter.web.auth.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 인증")
public class VerifyEmailRequestDocument {

    @Schema(description = "이메일")
    public String email;

    @Schema(description = "인증 코드")
    public String code;
}
