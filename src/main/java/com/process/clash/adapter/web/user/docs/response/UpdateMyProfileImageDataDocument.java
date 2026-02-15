package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 이미지 수정 데이터")
public class UpdateMyProfileImageDataDocument {

    @Schema(description = "변경된 프로필 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png")
    public String profileImageUrl;
}
