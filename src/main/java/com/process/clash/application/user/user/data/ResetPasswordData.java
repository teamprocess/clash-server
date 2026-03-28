package com.process.clash.application.user.user.data;

public class ResetPasswordData {

    public record SendCommand(String email, String state, String redirectUri) {
        public boolean hasAuthContext() {
            return state != null && !state.isBlank() && redirectUri != null && !redirectUri.isBlank();
        }
    }

    public record ResetCommand(String token, String newPassword) {}

    public record ResetResult(String state, String redirectUri) {}

}
