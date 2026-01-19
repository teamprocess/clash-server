package com.process.clash.adapter.web.auth.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청")
public class SignUpRequestDoc {

    @Schema(description = "아이디")
    public String username;

    @Schema(description = "비밀번호")
    public String password;

    @Schema(description = "이름")
    public String name;
}
