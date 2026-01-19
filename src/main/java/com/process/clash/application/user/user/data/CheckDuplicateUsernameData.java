package com.process.clash.application.user.user.data;

public class CheckDuplicateUsernameData {

    public record Command(
            String username
    ) {}
}
