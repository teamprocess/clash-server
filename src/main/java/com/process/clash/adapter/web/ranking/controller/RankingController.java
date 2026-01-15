package com.process.clash.adapter.web.ranking.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.mainpage.dto.mainpage.GetRankingDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.mainpage.data.mainpage.GetRankingData;
import com.process.clash.application.mainpage.port.in.mainpage.GetRankingUseCase;
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
public class RankingController {

    private final GetRankingUseCase getRankingUseCase;

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
}
