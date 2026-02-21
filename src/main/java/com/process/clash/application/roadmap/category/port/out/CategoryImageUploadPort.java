package com.process.clash.application.roadmap.category.port.out;

public interface CategoryImageUploadPort {

    PresignedUpload issueUploadUrl(Long categoryId, String extension, String contentType);

    boolean isValidCategoryImageUrl(Long categoryId, String categoryImageUrl);

    record PresignedUpload(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            long expiresInSeconds,
            String httpMethod
    ) {}
}
