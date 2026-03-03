package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "내 계정/프로필 조회 응답",
        example = """
            {
              "success": true,
              "message": "내 프로필을 성공적으로 조회했습니다.",
              "data": {
                "id": 1,
                "createdAt": "2025-01-02T09:00:00Z",
                "updatedAt": "2025-01-05T12:30:00Z",
                "username": "process123",
                "name": "홍길동",
                "email": "hong@example.com",
                "role": "USER",
                "profileImage": "https://cdn.example.com/profile/1.png",
                "totalExp": 1200,
                "totalCookie": 300,
                "totalToken": 0,
                "major": "SERVER",
                "userStatus": "ACTIVE",
                "githubLinked": true,
                "activityStatus": "ONLINE",
                "equippedItems": {
                  "insigma": {
                    "id": 1,
                    "name": "기본 인시그니아",
                    "image": "https://cdn.example.com/items/insignia.png"
                  },
                  "nameplate": null,
                  "banner": null
                }
              }
            }
            """
)
public class GetMyProfileResponseDocument extends SuccessResponseDocument {

    @Schema(description = "내 계정/프로필")
    public GetMyProfileDataDocument data;
}
