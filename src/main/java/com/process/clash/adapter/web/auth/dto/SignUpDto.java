package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.SignUpData;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class SignUpDto {
    @Schema(name = "SignUpDtoRequest")
    public record Request(
            @NotBlank(message = "유저 아이디는 필수 입력값입니다.")
            String username,
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            String email,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            String password,
            @NotBlank(message = "이름은 필수 입력값입니다.")
            String name
    ) {
        public SignUpData.Command toCommand() {
            return new SignUpData.Command(username, email, password, name);
        }
    }
}