package com.process.clash.adapter.aws;

import com.process.clash.application.user.user.port.out.ProfileImageUploadPort;
import com.process.clash.infrastructure.config.S3Properties;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3ProfileImageUploadAdapter implements ProfileImageUploadPort {

    private final S3Presigner s3Presigner;
    private final S3Properties properties;

    @Override
    public PresignedUpload issueUploadUrl(Long userId, String extension, String contentType) {
        String objectKey = buildObjectKey(userId, extension);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(objectKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(properties.getPresignExpirationSeconds()))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);

        return new PresignedUpload(
                presigned.url().toString(),
                objectKey,
                resolveFileUrl(objectKey),
                properties.getPresignExpirationSeconds(),
                presigned.httpRequest().method().name()
        );
    }

    private String buildObjectKey(Long userId, String extension) {
        String prefix = normalizePrefix(properties.getProfileImagePrefix());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String keyBody = "user-" + userId + "/" + uuid + "." + extension;

        if (prefix.isBlank()) {
            return keyBody;
        }

        return prefix + "/" + keyBody;
    }

    private String resolveFileUrl(String objectKey) {
        String publicBaseUrl = properties.getPublicBaseUrl();
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return removeTrailingSlash(publicBaseUrl.trim()) + "/" + objectKey;
        }

        return "https://%s.s3.%s.amazonaws.com/%s".formatted(
                properties.getBucket(),
                properties.getRegion(),
                objectKey
        );
    }

    private String normalizePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return "";
        }

        String normalized = prefix.trim();

        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }

    private String removeTrailingSlash(String baseUrl) {
        String normalized = baseUrl;
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
