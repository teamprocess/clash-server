package com.process.clash.adapter.web.roadmap.v2.chapter.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.chapter.docs.controller.ChapterV2ControllerDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.GetChapterV2DetailsDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.chapter.data.GetChapterV2DetailsData;
import com.process.clash.application.roadmap.v2.chapter.port.in.GetChapterV2DetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/chapters")
@RequiredArgsConstructor
public class ChapterV2Controller implements ChapterV2ControllerDocument {

    private final GetChapterV2DetailsUseCase getChapterV2DetailsUseCase;

    @GetMapping("/{chapterId}/details")
    public ApiResponse<GetChapterV2DetailsDto.Response> getChapterDetails(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId
    ) {
        GetChapterV2DetailsData.Command command = new GetChapterV2DetailsData.Command(actor, chapterId);
        GetChapterV2DetailsData.Result result = getChapterV2DetailsUseCase.execute(command);
        GetChapterV2DetailsDto.Response response = GetChapterV2DetailsDto.Response.from(result);
        return ApiResponse.success(response, "챕터 상세 조회를 성공했습니다.");
    }
}
