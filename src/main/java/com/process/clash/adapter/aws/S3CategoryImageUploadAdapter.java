package com.process.clash.adapter.aws;

import com.process.clash.application.roadmap.category.port.out.CategoryImageUploadPort;
import com.process.clash.infrastructure.config.aws.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class S3CategoryImageUploadAdapter implements CategoryImageUploadPort {

    private static final Pattern CATEGORY_IMAGE_FILE_NAME_PATTERN =
            Pattern.compile("^[a-f0-9]{32}\\.(jpg|png|webp|gif)$");

    private final S3Presigner s3Presigner;
    private final S3Properties properties;

    @Override
    public PresignedUpload issueUploadUrl(Long categoryId, String extension, String contentType) {
        String objectKey = buildObjectKey(categoryId, extension);

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

    @Override
    public boolean isValidCategoryImageUrl(Long categoryId, String categoryImageUrl) {
        if (categoryId == null || categoryImageUrl == null) {
            return false;
        }

        String normalizedUrl = categoryImageUrl.trim();
        if (normalizedUrl.isBlank()) {
            return false;
        }

        String expectedUrlPrefix = resolveFileUrl(buildObjectKeyPrefix(categoryId)) + "/";
        if (!normalizedUrl.startsWith(expectedUrlPrefix)) {
            return false;
        }

        String fileName = normalizedUrl.substring(expectedUrlPrefix.length());
        if (fileName.contains("/")) {
            return false;
        }

        return CATEGORY_IMAGE_FILE_NAME_PATTERN.matcher(fileName).matches();
    }

    private String buildObjectKey(Long categoryId, String extension) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return buildObjectKeyPrefix(categoryId) + "/" + uuid + "." + extension;
    }

    private String buildObjectKeyPrefix(Long categoryId) {
        String prefix = normalizePrefix(properties.getCategoryImagePrefix());
        String categoryDirectory = "category-" + categoryId;
        if (prefix.isBlank()) {
            return categoryDirectory;
        }
        return prefix + "/" + categoryDirectory;
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
        int start = 0;
        int end = normalized.length();

        while (start < end && normalized.charAt(start) == '/') {
            start++;
        }
        while (end > start && normalized.charAt(end - 1) == '/') {
            end--;
        }

        return normalized.substring(start, end);
    }

    private String removeTrailingSlash(String baseUrl) {
        int end = baseUrl.length();
        while (end > 0 && baseUrl.charAt(end - 1) == '/') {
            end--;
        }
        return baseUrl.substring(0, end);
    }
}
