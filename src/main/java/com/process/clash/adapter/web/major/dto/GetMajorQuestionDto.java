package com.process.clash.adapter.web.major.dto;

import com.process.clash.application.major.data.GetMajorQuestionData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetMajorQuestionDto {

    @Schema(name = "GetMajorQuestionDtoResponse")

    public record Response(
            List<MajorQuestionVo> majorQuestions
    ) {
        public static Response from(GetMajorQuestionData.Result result) {

            // Result 내부의 List<MajorQuestionVo>를 Web용 List<MajorQuestionVo>로 변환
            List<MajorQuestionVo> vos = result.majorQuestionVos().stream()
                    .map(MajorQuestionVo::from)
                    .toList();

            return new Response(vos);
        }
    }

    public record MajorQuestionVo(
            Long id,
            String content,
            MajorWeightVo weight
    ) {
        // Result 내부의 개별 VO를 인자로 받아 변환
        public static MajorQuestionVo from(GetMajorQuestionData.Result.MajorQuestionVo vo) {

            return new MajorQuestionVo(
                    vo.id(),
                    vo.content(),
                    MajorWeightVo.from(vo.weightVo())
            );
        }
    }

    public record MajorWeightVo(
            Integer web,
            Integer app,
            Integer server,
            Integer ai,
            Integer game
    ) {
        // Result 내부의 WeightVo를 인자로 받아 변환
        public static MajorWeightVo from(GetMajorQuestionData.Result.MajorQuestionVo.WeightVo vo) {

            return new MajorWeightVo(
                    vo.web(),
                    vo.app(),
                    vo.server(),
                    vo.ai(),
                    vo.game()
            );
        }
    }
}