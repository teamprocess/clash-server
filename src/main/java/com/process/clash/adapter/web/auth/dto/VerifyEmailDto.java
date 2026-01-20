package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.VerifyEmailData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyEmailDto {

    public record Request(
            @NotBlank
            @Size(min = 6, max = 6, message = "인증 코드는 6자리입니다.")
            String code
    ) {
        public VerifyEmailData.Command toCommand(String token) {
            return new VerifyEmailData.Command(token, this.code);
        }
    }
}
