package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.IssueCategoryImageUploadUrlData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class IssueCategoryImageUploadUrlDto {

    @Schema(name = "IssueCategoryImageUploadUrlDtoRequest")
    public record Request(
            @NotBlank(message = "파일명은 필수 입력값입니다.")
            String fileName,
            @NotBlank(message = "contentType은 필수 입력값입니다.")
            String contentType
    ) {
        public IssueCategoryImageUploadUrlData.Command toCommand(Actor actor, Long categoryId) {
            return new IssueCategoryImageUploadUrlData.Command(actor, categoryId, fileName, contentType);
        }
    }

    @Schema(name = "IssueCategoryImageUploadUrlDtoResponse")
    public record Response(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            String httpMethod,
            String contentType,
            long expiresInSeconds
    ) {
        public static Response from(IssueCategoryImageUploadUrlData.Result result) {
            return new Response(
                    result.uploadUrl(),
                    result.objectKey(),
                    result.fileUrl(),
                    result.httpMethod(),
                    result.contentType(),
                    result.expiresInSeconds()
            );
        }
    }
}
