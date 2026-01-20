package com.process.clash.application.user.user.data;

public class VerifyEmailData {

    public record Command(
            String token,
            String code
    ) {}
}
