package com.process.clash.adapter.web.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class SignInDto {
    public record Request(
            @NotNull(message = "유저 아이디는 필수 입력값입니다.")
            String username,
            @NotNull(message = "비밀번호는 필수 입력값입니다.")
            String password
    ) { }

    @Builder // TODO: Builder 제거
    public record Response(
            Long id,
            String username,
            String name
    ) {
    }
}
