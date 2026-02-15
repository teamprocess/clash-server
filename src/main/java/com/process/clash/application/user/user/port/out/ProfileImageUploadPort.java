package com.process.clash.application.user.user.port.out;

public interface ProfileImageUploadPort {

    PresignedUpload issueUploadUrl(Long userId, String extension, String contentType);

    record PresignedUpload(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            long expiresInSeconds,
            String httpMethod
    ) {
    }
}
