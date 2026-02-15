package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.data.IssueProfileImageUploadUrlData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class IssueProfileImageUploadUrlDto {

    @Schema(name = "IssueProfileImageUploadUrlRequest")
    public record Request(
            @NotBlank(message = "파일명은 필수 입력값입니다.")
            String fileName,
            @NotBlank(message = "contentType은 필수 입력값입니다.")
            String contentType
    ) {
        public IssueProfileImageUploadUrlData.Command toCommand(Actor actor) {
            return new IssueProfileImageUploadUrlData.Command(actor, fileName, contentType);
        }
    }

    public record Response(
            String uploadUrl,
            String objectKey,
            String fileUrl,
            String httpMethod,
            String contentType,
            long expiresInSeconds
    ) {
        public static Response from(IssueProfileImageUploadUrlData.Result result) {
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
