package com.process.clash.adapter.web.auth.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public class SignInRequestDoc {

    @Schema(description = "아이디")
    public String username;

    @Schema(description = "비밀번호")
    public String password;

    @Schema(description = "자동 로그인 여부")
    public Boolean rememberMe;
}
