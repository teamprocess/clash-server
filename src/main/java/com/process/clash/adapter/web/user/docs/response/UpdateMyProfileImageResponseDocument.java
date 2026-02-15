package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "프로필 이미지 수정 응답",
        example = """
            {
              \"success\": true,
              \"message\": \"프로필 이미지 수정을 성공했습니다.\",
              \"data\": {
                \"profileImageUrl\": \"https://bucket.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png\"
              }
            }
            """
)
public class UpdateMyProfileImageResponseDocument extends SuccessResponseDocument {

    @Schema(description = "프로필 이미지 수정 결과")
    public UpdateMyProfileImageDataDocument data;
}
