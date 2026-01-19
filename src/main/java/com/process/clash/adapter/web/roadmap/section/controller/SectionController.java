package com.process.clash.adapter.web.roadmap.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.section.docs.controller.SectionControllerDocument;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionDetailsDto;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionPreviewDto;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionsDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;
import com.process.clash.application.roadmap.section.data.GetSectionPreviewData;
import com.process.clash.application.roadmap.section.data.GetSectionsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionDetailsUseCase;
import com.process.clash.application.roadmap.section.port.in.GetSectionPreviewUseCase;
import com.process.clash.application.roadmap.section.port.in.GetSectionsUseCase;
import com.process.clash.domain.common.enums.Major;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController implements SectionControllerDocument {

    private final GetSectionsUseCase getSectionsUseCase;
    private final GetSectionPreviewUseCase getSectionPreviewUseCase;
    private final GetSectionDetailsUseCase getSectionDetailsUseCase;

    @GetMapping
    public ApiResponse<GetSectionsDto.Response> getSections(
            @AuthenticatedActor Actor actor,
            @RequestParam Major major
    ) {
        GetSectionsData.Command command = new GetSectionsData.Command(actor, major);
        GetSectionsData.Result result = getSectionsUseCase.execute(command);
        GetSectionsDto.Response response = GetSectionsDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 목록 조회를 성공했습니다.");
    }

    @GetMapping("/{sectionId}/preview")
    public ApiResponse<GetSectionPreviewDto.Response> getSectionPreview(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        GetSectionPreviewData.Command command = new GetSectionPreviewData.Command(actor, sectionId);
        GetSectionPreviewData.Result result = getSectionPreviewUseCase.execute(command);
        GetSectionPreviewDto.Response response = GetSectionPreviewDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 미리보기 조회를 성공했습니다.");
    }

    @GetMapping("/{sectionId}/details")
    public ApiResponse<GetSectionDetailsDto.Response> getSectionDetails(
            @AuthenticatedActor Actor actor,
            @PathVariable Long sectionId
    ) {
        GetSectionDetailsData.Command command = new GetSectionDetailsData.Command(actor, sectionId);
        GetSectionDetailsData.Result result = getSectionDetailsUseCase.execute(command);
        GetSectionDetailsDto.Response response = GetSectionDetailsDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 상세 조회를 성공했습니다.");
    }
}
