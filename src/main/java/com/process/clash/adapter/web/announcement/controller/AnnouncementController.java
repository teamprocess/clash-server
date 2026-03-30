package com.process.clash.adapter.web.announcement.controller;

import com.process.clash.adapter.web.announcement.docs.controller.AnnouncementControllerDocument;
import com.process.clash.adapter.web.announcement.dto.GetActiveAnnouncementsDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;
import com.process.clash.application.announcement.port.in.GetActiveAnnouncementsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementControllerDocument {

    private final GetActiveAnnouncementsUseCase getActiveAnnouncementsUseCase;

    @Override
    @GetMapping("/active")
    public ApiResponse<GetActiveAnnouncementsDto.Response> getActiveAnnouncements() {
        GetActiveAnnouncementsData.Result result = getActiveAnnouncementsUseCase.execute();
        GetActiveAnnouncementsDto.Response response = GetActiveAnnouncementsDto.Response.from(result);
        return ApiResponse.success(response, "활성 공지 목록을 성공적으로 조회했습니다.");
    }
}
