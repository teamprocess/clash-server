package com.process.clash.application.realtime.data;

public final class IssueSocketTokenData {

    private IssueSocketTokenData() {
    }

    public record Result(String token, long expiresInSeconds) {
    }
}
