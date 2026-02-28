package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "라이벌 전체 조회 응답")
public class FindAllRivalsResponseDocument extends SuccessMessageResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌 전체 목록을 성공적으로 조회했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "라이벌 목록")
        public List<RivalInfoDoc> rivals;
    }

    public static class RivalInfoDoc {
        @Schema(description = "라이벌 관계 ID", example = "1")
        public Long rivalId;

        @Schema(description = "라이벌 유저 깃허브 ID", example = "mongryong")
        public String githubId;

        @Schema(description = "라이벌 유저 이름", example = "이몽룡")
        public String name;

        @Schema(description = "라이벌 유저 프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;

        @Schema(description = "라이벌 연결 상태", example = "ACCEPTED")
        public String rivalLinkingStatus;
    }
}
