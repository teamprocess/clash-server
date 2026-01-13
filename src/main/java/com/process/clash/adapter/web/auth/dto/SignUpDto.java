package com.process.clash.adapter.web.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignUpDto {
    public record Request(
            @NotBlank(message = "유저 아이디는 필수 입력값입니다.")
            String username,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            String password,
            @NotBlank(message = "이름은 필수 입력값입니다.")
            String name
    ) {}
}