package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.ResetPasswordData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDto {

    public record SendRequest(
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email
    ) {
        public ResetPasswordData.SendCommand toCommand() {
            return new ResetPasswordData.SendCommand(email);
        }
    }

    public record ResetRequest(
            @NotBlank(message = "토큰은 필수 입력값입니다.")
            String token,
            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            @Size(min = 8, max = 100, message = "비밀번호는 8~100자여야 합니다.")
            String newPassword
    ) {
        public ResetPasswordData.ResetCommand toCommand() {
            return new ResetPasswordData.ResetCommand(token, newPassword);
        }
    }
}
