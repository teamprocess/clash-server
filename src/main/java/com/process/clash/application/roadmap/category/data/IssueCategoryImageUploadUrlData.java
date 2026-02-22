package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;

public final class IssueCategoryImageUploadUrlData {

    private IssueCategoryImageUploadUrlData() {}

    public record Command(
            Actor actor,
            Long categoryId,
            String fileName,
            String contentType
    ) {}

    public record Result(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            String httpMethod,
            String contentType,
            long expiresInSeconds
    ) {}
}
