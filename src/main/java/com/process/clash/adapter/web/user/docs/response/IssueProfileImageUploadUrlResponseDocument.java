package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "프로필 이미지 업로드 URL 발급 응답",
        example = """
            {
              \"success\": true,
              \"message\": \"프로필 이미지 업로드 URL 발급을 성공했습니다.\",
              \"data\": {
                \"uploadUrl\": \"https://bucket.s3.ap-northeast-2.amazonaws.com/path/file.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&...\",
                \"objectKey\": \"users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png\",
                \"fileUrl\": \"https://bucket.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png\",
                \"httpMethod\": \"PUT\",
                \"contentType\": \"image/png\",
                \"expiresInSeconds\": 300
              }
            }
            """
)
public class IssueProfileImageUploadUrlResponseDocument extends SuccessResponseDocument {

    @Schema(description = "프로필 이미지 업로드 URL 발급 결과")
    public IssueProfileImageUploadUrlDataDocument data;
}
