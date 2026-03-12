package com.process.clash.adapter.web.roadmap.v2.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.section.docs.controller.SectionV2ControllerDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2DetailsDto;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2ListDto;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2PreviewDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2PreviewData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2DetailsUseCase;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2ListUseCase;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2PreviewUseCase;
import com.process.clash.domain.common.enums.Major;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/sections")
@RequiredArgsConstructor
public class SectionV2Controller implements SectionV2ControllerDocument {

    private final GetSectionV2ListUseCase getSectionV2ListUseCase;
    private final GetSectionV2PreviewUseCase getSectionV2PreviewUseCase;
    private final GetSectionV2DetailsUseCase getSectionV2DetailsUseCase;

    @GetMapping
    public ApiResponse<GetSectionV2ListDto.Response> getSections(
            @AuthenticatedActor Actor actor,
            @RequestParam Major major
    ) {
        GetSectionV2ListData.Command command = new GetSectionV2ListData.Command(actor, major);
        GetSectionV2ListData.Result result = getSectionV2ListUseCase.execute(command);
        GetSectionV2ListDto.Response response = GetSectionV2ListDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 목록 조회를 성공했습니다.");
    }

    @GetMapping("/{sectionId}/preview")
    public ApiResponse<GetSectionV2PreviewDto.Response> getSectionPreview(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        GetSectionV2PreviewData.Command command = new GetSectionV2PreviewData.Command(actor, sectionId);
        GetSectionV2PreviewData.Result result = getSectionV2PreviewUseCase.execute(command);
        GetSectionV2PreviewDto.Response response = GetSectionV2PreviewDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 미리보기 조회를 성공했습니다.");
    }

    @GetMapping("/{sectionId}/details")
    public ApiResponse<GetSectionV2DetailsDto.Response> getSectionDetails(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        GetSectionV2DetailsData.Command command = new GetSectionV2DetailsData.Command(actor, sectionId);
        GetSectionV2DetailsData.Result result = getSectionV2DetailsUseCase.execute(command);
        GetSectionV2DetailsDto.Response response = GetSectionV2DetailsDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 상세 조회를 성공했습니다.");
    }
}
