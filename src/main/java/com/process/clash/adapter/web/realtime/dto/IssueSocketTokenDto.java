package com.process.clash.adapter.web.realtime.dto;

import com.process.clash.application.realtime.data.IssueSocketTokenData;

public final class IssueSocketTokenDto {

    private IssueSocketTokenDto() {
    }

    public record Response(String token, long expiresInSeconds) {
        public static Response from(IssueSocketTokenData.Result result) {
            return new Response(result.token(), result.expiresInSeconds());
        }
    }
}
