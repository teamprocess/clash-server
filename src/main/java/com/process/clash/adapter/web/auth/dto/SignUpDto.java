package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.SignUpData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpDto {
    @Schema(name = "SignUpDtoRequest")
    public record Request(
            @NotBlank(message = "유저네임은 필수 입력값입니다.")
            @Size(min = 3, max = 20, message = "유저네임은 3~20자여야 합니다.")
            @Pattern(
                    regexp = "^[a-zA-Z0-9_-]+$",
                    message = "유저네임은 영문, 숫자, _, -만 사용 가능합니다."
            )
            String username,
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            String email,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            @Size(min = 8, max = 100, message = "비밀번호는 8~100자여야 합니다.")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                    message = "비밀번호는 영문과 숫자를 포함해야 합니다."
            )
            String password,
            @NotBlank(message = "이름은 필수 입력값입니다.")
            @Size(min = 1, max = 50, message = "이름은 1~50자여야 합니다.")
            String name
    ) {
        public SignUpData.Command toCommand() {
            return new SignUpData.Command(username, email, password, name);
        }
    }
}