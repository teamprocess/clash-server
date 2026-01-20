package com.process.clash.adapter.web.roadmap.chapter.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.chapter.dto.GetChapterDetailsDto;
import com.process.clash.adapter.web.roadmap.chapter.docs.controller.ChapterControllerDocument;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;
import com.process.clash.application.roadmap.chapter.port.in.GetChapterDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController implements ChapterControllerDocument {

    private final GetChapterDetailsUseCase getChapterDetailsUseCase;

    @GetMapping("/{chapterId}/details")
    public ApiResponse<GetChapterDetailsDto.Response> getChapterDetails(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId
    ) {
        GetChapterDetailsData.Command command = new GetChapterDetailsData.Command(actor, chapterId);
        GetChapterDetailsData.Result result = getChapterDetailsUseCase.execute(command);
        GetChapterDetailsDto.Response response = GetChapterDetailsDto.Response.from(result);
        return ApiResponse.success(response, "챕터 상세 조회를 성공했습니다.");
    }
}
