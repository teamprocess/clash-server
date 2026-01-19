package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.VerifyEmailData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyEmailDto {

    public record Request(
            @Email(message = "이메일 형식이 맞지 않습니다.")
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            String email,

            @NotBlank
            @Size(min = 6, max = 6, message = "인증 코드는 6자리입니다.")
            String code
    ) {
        public VerifyEmailData.Command toCommand() {
            return new VerifyEmailData.Command(this.email, this.code);
        }
    }
}
