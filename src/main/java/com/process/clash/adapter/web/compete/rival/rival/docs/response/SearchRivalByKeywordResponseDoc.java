package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "라이벌 검색 응답")
public class SearchRivalByKeywordResponseDoc extends SuccessMessageResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "키워드를 이용하여 라이벌 등록가능한 유저를 성공적으로 조회했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "검색된 유저 목록")
        public List<UserDoc> users;
    }

    public static class UserDoc {
        @Schema(description = "사용자 ID", example = "3")
        public Long userId;

        @Schema(description = "이름", example = "성춘향")
        public String userName;

        @Schema(description = "깃허브 아이디", example = "chunhyang")
        public String gitHubId;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/3.png")
        public String profileImage;
    }
}