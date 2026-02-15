package com.process.clash.application.user.user.data;

import com.process.clash.application.common.actor.Actor;

public final class IssueProfileImageUploadUrlData {

    private IssueProfileImageUploadUrlData() {
    }

    public record Command(
            Actor actor,
            String fileName,
            String contentType
    ) {
    }

    public record Result(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            String httpMethod,
            String contentType,
            long expiresInSeconds
    ) {
    }
}
