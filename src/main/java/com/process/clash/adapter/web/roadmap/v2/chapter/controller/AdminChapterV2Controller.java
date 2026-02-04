package com.process.clash.adapter.web.roadmap.v2.chapter.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.CreateChapterV2Dto;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.UpdateChapterV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.chapter.data.CreateChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.data.DeleteChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.data.UpdateChapterV2Data;
import com.process.clash.application.roadmap.v2.chapter.port.in.CreateChapterV2UseCase;
import com.process.clash.application.roadmap.v2.chapter.port.in.DeleteChapterV2UseCase;
import com.process.clash.application.roadmap.v2.chapter.port.in.UpdateChapterV2UseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/chapters")
@RequiredArgsConstructor
public class AdminChapterV2Controller {

    private final CreateChapterV2UseCase createChapterV2UseCase;
    private final UpdateChapterV2UseCase updateChapterV2UseCase;
    private final DeleteChapterV2UseCase deleteChapterV2UseCase;

    @PostMapping
    public ApiResponse<CreateChapterV2Dto.Response> createChapter(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateChapterV2Dto.Request request
    ) {
        CreateChapterV2Data.Command command = request.toCommand(actor);
        CreateChapterV2Data.Result result = createChapterV2UseCase.execute(command);
        CreateChapterV2Dto.Response response = CreateChapterV2Dto.Response.from(result);
        return ApiResponse.created(response, "챕터 생성을 성공했습니다.");
    }

    @PatchMapping("/{chapterId}")
    public ApiResponse<UpdateChapterV2Dto.Response> updateChapter(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId,
            @Valid @RequestBody UpdateChapterV2Dto.Request request
    ) {
        UpdateChapterV2Data.Command command = request.toCommand(actor, chapterId);
        UpdateChapterV2Data.Result result = updateChapterV2UseCase.execute(command);
        UpdateChapterV2Dto.Response response = UpdateChapterV2Dto.Response.from(result);
        return ApiResponse.success(response, "챕터 수정을 성공했습니다.");
    }

    @DeleteMapping("/{chapterId}")
    public ApiResponse<Void> deleteChapter(
            @AuthenticatedActor Actor actor,
            @PathVariable Long chapterId
    ) {
        DeleteChapterV2Data.Command command = new DeleteChapterV2Data.Command(actor, chapterId);
        deleteChapterV2UseCase.execute(command);
        return ApiResponse.success("챕터 삭제를 성공했습니다.");
    }
}
