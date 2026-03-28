package com.process.clash.application.user.user.data;

public class ResetPasswordData {

    public record SendCommand(String email) {}

    public record ResetCommand(String token, String newPassword) {}

    public record ResetResult(String state, String redirectUri) {}

}
