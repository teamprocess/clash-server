package com.process.clash.adapter.web.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.section.dto.CreateSectionDto;
import com.process.clash.adapter.web.section.dto.UpdateSectionDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sections")
@RequiredArgsConstructor
public class AdminSectionController {

    // TODO: Inject use cases when implemented
    // private final CreateSectionUseCase createSectionUseCase;
    // private final UpdateSectionUseCase updateSectionUseCase;
    // private final DeleteSectionUseCase deleteSectionUseCase;

    @PostMapping
    public ApiResponse<CreateSectionDto.Response> createSection(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateSectionDto.Request request
    ) {
        // TODO: Implement use case
        // CreateSectionData.Command command = request.toCommand(actor);
        // CreateSectionData.Result result = createSectionUseCase.execute(command);
        // CreateSectionDto.Response response = CreateSectionDto.Response.from(result);
        // return ApiResponse.created(response, "로드맵 생성을 성공했습니다.");

        // Temporary response
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
        // TODO: Implement use case
        // UpdateSectionData.Command command = request.toCommand(actor, sectionId);
        // UpdateSectionData.Result result = updateSectionUseCase.execute(command);
        // UpdateSectionDto.Response response = UpdateSectionDto.Response.from(result);
        // return ApiResponse.success(response, "로드맵 수정을 성공했습니다.");

        // Temporary response
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
        // TODO: Implement use case
        // DeleteSectionData.Command command = new DeleteSectionData.Command(actor, sectionId);
        // deleteSectionUseCase.execute(command);
        // return ApiResponse.success("로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)");

        // Temporary response
        return ApiResponse.success("로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)");
    }
}
