package com.process.clash.adapter.web.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.process.clash.application.user.user.data.SignInData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class SignInDto {
    @Schema(name = "SignInDtoRequest")
    public record Request(
            @NotBlank(message = "유저네임은 필수 입력값입니다.")
            @Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
            String username,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            @Size(min = 8, max = 100, message = "비밀번호는 8~100자여야 합니다.")
            String password,
            @JsonProperty("rememberMe")
            boolean rememberMe
    ) { }

    @Schema(name = "SignInDtoResponse")

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
