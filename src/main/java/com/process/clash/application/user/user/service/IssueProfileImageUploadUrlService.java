package com.process.clash.application.user.user.service;

import com.process.clash.application.user.user.data.IssueProfileImageUploadUrlData;
import com.process.clash.application.user.user.exception.exception.badrequest.InvalidProfileImageUploadRequestException;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.in.IssueProfileImageUploadUrlUseCase;
import com.process.clash.application.user.user.port.out.ProfileImageUploadPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueProfileImageUploadUrlService implements IssueProfileImageUploadUrlUseCase {

    private static final Map<String, String> CONTENT_TYPE_EXTENSION = Map.of(
            "image/jpeg", "jpg",
            "image/jpg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");

    private final UserRepositoryPort userRepositoryPort;
    private final ProfileImageUploadPort profileImageUploadPort;

    @Override
    public IssueProfileImageUploadUrlData.Result execute(IssueProfileImageUploadUrlData.Command command) {
        if (command == null || command.actor() == null || command.actor().id() == null) {
            throw new InvalidProfileImageUploadRequestException();
        }

        String fileName = normalizeText(command.fileName());
        if (fileName == null) {
            throw new InvalidProfileImageUploadRequestException();
        }

        userRepositoryPort.findById(command.actor().id())
                .orElseThrow(UserNotFoundException::new);

        String normalizedContentType = normalizeContentType(command.contentType());
        String extension = resolveExtension(fileName, normalizedContentType);

        ProfileImageUploadPort.PresignedUpload presigned = profileImageUploadPort.issueUploadUrl(
                command.actor().id(),
                extension,
                normalizedContentType
        );

        return new IssueProfileImageUploadUrlData.Result(
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
            throw new InvalidProfileImageUploadRequestException();
        }

        int separatorIndex = normalized.indexOf(';');
        if (separatorIndex >= 0) {
            normalized = normalized.substring(0, separatorIndex).trim();
        }

        if (!CONTENT_TYPE_EXTENSION.containsKey(normalized)) {
            throw new InvalidProfileImageUploadRequestException();
        }

        return normalized;
    }

    private String resolveExtension(String fileName, String contentType) {
        String fromFileName = extractExtension(fileName);
        if (fromFileName != null) {
            if ("jpeg".equals(fromFileName)) {
                return "jpg";
            }
            if (ALLOWED_EXTENSIONS.contains(fromFileName)) {
                return fromFileName;
            }
        }

        String fromContentType = CONTENT_TYPE_EXTENSION.get(contentType);
        if (fromContentType == null) {
            throw new InvalidProfileImageUploadRequestException();
        }

        return fromContentType;
    }

    private String extractExtension(String fileName) {
        String normalized = fileName.replace("\\", "/");
        int lastSlashIndex = normalized.lastIndexOf('/');
        String baseName = lastSlashIndex >= 0 ? normalized.substring(lastSlashIndex + 1) : normalized;

        int lastDotIndex = baseName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == baseName.length() - 1) {
            return null;
        }

        return baseName.substring(lastDotIndex + 1).toLowerCase(Locale.ROOT);
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
