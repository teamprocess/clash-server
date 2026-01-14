package com.process.clash.adapter.web.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.process.clash.application.user.data.SignInData;
import jakarta.validation.constraints.NotBlank;

public class SignInDto {
    public record Request(
            @NotBlank(message = "유저 아이디는 필수 입력값입니다.")
            String username,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            String password,
            @JsonProperty("rememberMe")
            boolean rememberMe
    ) { }

    public record Response(
            Long id,
            String username,
            String name
    ) {
        public static Response fromResult(SignInData.Result result) {
            return new Response(
                    result.id(),
                    result.username(),
                    result.name()
            );
        }
    }
}
