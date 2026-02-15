package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 이미지 업로드 URL 데이터")
public class IssueProfileImageUploadUrlDataDocument {

    @Schema(description = "S3 presigned PUT URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/path/file.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&...")
    public String uploadUrl;

    @Schema(description = "S3 Object Key", example = "users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png")
    public String objectKey;

    @Schema(description = "업로드 완료 후 접근 가능한 파일 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png")
    public String fileUrl;

    @Schema(description = "업로드 HTTP 메서드", example = "PUT")
    public String httpMethod;

    @Schema(description = "업로드 시 반드시 함께 전송할 Content-Type", example = "image/png")
    public String contentType;

    @Schema(description = "URL 만료(초)", example = "300")
    public long expiresInSeconds;
}
