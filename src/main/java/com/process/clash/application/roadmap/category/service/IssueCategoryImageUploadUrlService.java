package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.IssueCategoryImageUploadUrlData;
import com.process.clash.application.roadmap.category.exception.exception.badrequest.InvalidCategoryImageUploadRequestException;
import com.process.clash.application.roadmap.category.exception.exception.notfound.CategoryNotFoundException;
import com.process.clash.application.roadmap.category.port.in.IssueCategoryImageUploadUrlUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryImageUploadPort;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IssueCategoryImageUploadUrlService implements IssueCategoryImageUploadUrlUseCase {

    private static final Map<String, String> CONTENT_TYPE_EXTENSION = Map.of(
            "image/jpeg", "jpg",
            "image/jpg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif"
    );

    private final CategoryRepositoryPort categoryRepository;
    private final CategoryImageUploadPort categoryImageUploadPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public IssueCategoryImageUploadUrlData.Result execute(IssueCategoryImageUploadUrlData.Command command) {
        checkAdminPolicy.check(command.actor());

        String fileName = normalizeText(command.fileName());
        if (fileName == null) {
            throw new InvalidCategoryImageUploadRequestException();
        }

        categoryRepository.findById(command.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        String normalizedContentType = normalizeContentType(command.contentType());
        String extension = resolveExtension(normalizedContentType);

        CategoryImageUploadPort.PresignedUpload presigned = categoryImageUploadPort.issueUploadUrl(
                command.categoryId(),
                extension,
                normalizedContentType
        );

        return new IssueCategoryImageUploadUrlData.Result(
                presigned.uploadUrl(),
                presigned.objectKey(),
                presigned.fileUrl(),
                presigned.httpMethod(),
                normalizedContentType,
                presigned.expiresInSeconds()
        );
    }

    private String normalizeContentType(String contentType) {
        String normalized = normalizeText(contentType);
        if (normalized == null) {
            throw new InvalidCategoryImageUploadRequestException();
        }

        int separatorIndex = normalized.indexOf(';');
        if (separatorIndex >= 0) {
            normalized = normalized.substring(0, separatorIndex).trim();
        }

        if (!CONTENT_TYPE_EXTENSION.containsKey(normalized)) {
            throw new InvalidCategoryImageUploadRequestException();
        }

        return normalized;
    }

    private String resolveExtension(String contentType) {
        String extension = CONTENT_TYPE_EXTENSION.get(contentType);
        if (extension == null) {
            throw new InvalidCategoryImageUploadRequestException();
        }
        return extension;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim().toLowerCase(Locale.ROOT);
        if (trimmed.isBlank()) {
            return null;
        }
        return trimmed;
    }
}
