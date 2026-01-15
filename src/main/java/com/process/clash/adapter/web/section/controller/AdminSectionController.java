package com.process.clash.adapter.web.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.section.dto.CreateSectionDto;
import com.process.clash.adapter.web.section.dto.UpdateSectionDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sections")
@RequiredArgsConstructor
public class AdminSectionController {

    // TODO: 구현 시 유스케이스 주입 필요
    // private final CreateSectionUseCase createSectionUseCase;
    // private final UpdateSectionUseCase updateSectionUseCase;
    // private final DeleteSectionUseCase deleteSectionUseCase;

    @PostMapping
    public ApiResponse<CreateSectionDto.Response> createSection(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateSectionDto.Request request
    ) {
        // TODO: 유스케이스 구현 필요
        // CreateSectionData.Command command = request.toCommand(actor);
        // CreateSectionData.Result result = createSectionUseCase.execute(command);
        // CreateSectionDto.Response response = CreateSectionDto.Response.from(result);
        // return ApiResponse.created(response, "로드맵 생성을 성공했습니다.");

        // 임시 응답
        CreateSectionDto.Response response = new CreateSectionDto.Response(
                null,
                request.major().name(),
                request.title(),
                request.category(),
                request.description(),
                request.keyPoints(),
                null
        );
        return ApiResponse.created(response, "로드맵 생성을 성공했습니다.");
    }

    @PatchMapping("/{sectionId}")
    public ApiResponse<UpdateSectionDto.Response> updateSection(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId,
            @Valid @RequestBody UpdateSectionDto.Request request
    ) {
        // TODO: 유스케이스 구현 필요
        // UpdateSectionData.Command command = request.toCommand(actor, sectionId);
        // UpdateSectionData.Result result = updateSectionUseCase.execute(command);
        // UpdateSectionDto.Response response = UpdateSectionDto.Response.from(result);
        // return ApiResponse.success(response, "로드맵 수정을 성공했습니다.");

        // 임시 응답
        UpdateSectionDto.Response response = new UpdateSectionDto.Response(
                sectionId,
                request.title(),
                request.category(),
                request.description(),
                request.keyPoints(),
                null
        );
        return ApiResponse.success(response, "로드맵 수정을 성공했습니다.");
    }

    @DeleteMapping("/{sectionId}")
    public ApiResponse<Void> deleteSection(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        // TODO: 유스케이스 구현 필요
        // DeleteSectionData.Command command = new DeleteSectionData.Command(actor, sectionId);
        // deleteSectionUseCase.execute(command);
        // return ApiResponse.success("로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)");

        // 임시 응답
        return ApiResponse.success("로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)");
    }
}
