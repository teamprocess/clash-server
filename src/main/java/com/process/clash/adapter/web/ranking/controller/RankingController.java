package com.process.clash.adapter.web.ranking.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.ranking.docs.controller.RankingControllerDocument;
import com.process.clash.adapter.web.ranking.dto.GetChapterRankingDto;
import com.process.clash.adapter.web.ranking.dto.GetRankingDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.ranking.data.GetChapterRankingData;
import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.port.in.GetChapterRankingUseCase;
import com.process.clash.application.ranking.port.in.GetRankingUseCase;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController implements RankingControllerDocument {

    private final GetRankingUseCase getRankingUseCase;
    private final GetChapterRankingUseCase getChapterRankingUseCase;

    // 대소고 랭킹
    // TODO: 추가 구현 필요합니다
    @GetMapping("/category/{category}/period/{period}")
    public ApiResponse<GetRankingDto.Response> getRanking(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category,
            @PathVariable PeriodCategory period
    ) {

        GetRankingData.Command command = GetRankingData.Command.from(actor, category, period);
        GetRankingData.Result result = getRankingUseCase.execute(command);
        GetRankingDto.Response response = GetRankingDto.Response.from(result);
        return ApiResponse.success(response, "내 EXP 획득 정보 분석 결과를 성공적으로 반환했습니다.");
    }

    @GetMapping("/chapters")
    public ApiResponse<GetChapterRankingDto.Response> getChapterRanking(@AuthenticatedActor Actor actor) {
        GetChapterRankingData.Command command = GetChapterRankingData.Command.from(actor);
        GetChapterRankingData.Result result = getChapterRankingUseCase.execute(command);
        GetChapterRankingDto.Response response = GetChapterRankingDto.Response.from(result);
        return ApiResponse.success(response, "챕터 완료 수 랭킹 조회를 성공했습니다.");

    }
}
