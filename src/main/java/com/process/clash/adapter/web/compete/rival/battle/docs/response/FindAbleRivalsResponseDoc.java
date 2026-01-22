package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "배틀 가능한 라이벌 조회 응답")
public class FindAbleRivalsResponseDoc extends SuccessMessageResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "배틀을 신청할 라이벌 목록을 성공적으로 반환하였습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "배틀 가능한 라이벌 목록")
        public List<RivalDoc> rivals;
    }

    public static class RivalDoc {
        @Schema(description = "라이벌 ID", example = "3")
        public Long id;

        @Schema(description = "이름", example = "이몽룡")
        public String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;
    }
}
