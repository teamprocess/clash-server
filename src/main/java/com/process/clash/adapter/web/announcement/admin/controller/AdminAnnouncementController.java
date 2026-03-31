package com.process.clash.adapter.web.announcement.admin.controller;

import com.process.clash.adapter.web.announcement.admin.docs.controller.AdminAnnouncementControllerDocument;
import com.process.clash.adapter.web.announcement.admin.dto.CreateAnnouncementAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.GetAllAnnouncementsAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.UpdateAnnouncementAdminDto;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.announcement.admin.data.CreateAnnouncementAdminData;
import com.process.clash.application.announcement.admin.data.DeleteAnnouncementAdminData;
import com.process.clash.application.announcement.admin.data.GetAllAnnouncementsAdminData;
import com.process.clash.application.announcement.admin.data.UpdateAnnouncementAdminData;
import com.process.clash.application.announcement.admin.port.in.CreateAnnouncementAdminUseCase;
import com.process.clash.application.announcement.admin.port.in.DeleteAnnouncementAdminUseCase;
import com.process.clash.application.announcement.admin.port.in.GetAllAnnouncementsAdminUseCase;
import com.process.clash.application.announcement.admin.port.in.UpdateAnnouncementAdminUseCase;
import com.process.clash.application.common.actor.Actor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/announcements")
@RequiredArgsConstructor
public class AdminAnnouncementController implements AdminAnnouncementControllerDocument {

    private final CreateAnnouncementAdminUseCase createAnnouncementAdminUseCase;
    private final GetAllAnnouncementsAdminUseCase getAllAnnouncementsAdminUseCase;
    private final UpdateAnnouncementAdminUseCase updateAnnouncementAdminUseCase;
    private final DeleteAnnouncementAdminUseCase deleteAnnouncementAdminUseCase;

    @Override
    @PostMapping
    public ApiResponse<CreateAnnouncementAdminDto.Response> createAnnouncement(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateAnnouncementAdminDto.Request request
    ) {
        CreateAnnouncementAdminData.Command command = request.toCommand(actor);
        CreateAnnouncementAdminData.Result result = createAnnouncementAdminUseCase.execute(command);
        return ApiResponse.created(CreateAnnouncementAdminDto.Response.from(result), "공지가 성공적으로 생성되었습니다.");
    }

    @Override
    @GetMapping
    public ApiResponse<GetAllAnnouncementsAdminDto.Response> getAllAnnouncements(
            @AuthenticatedActor Actor actor
    ) {
        GetAllAnnouncementsAdminData.Result result = getAllAnnouncementsAdminUseCase.execute();
        return ApiResponse.success(GetAllAnnouncementsAdminDto.Response.from(result), "공지 목록을 성공적으로 조회했습니다.");
    }

    @Override
    @PutMapping("/{id}")
    public ApiResponse<UpdateAnnouncementAdminDto.Response> updateAnnouncement(
            @AuthenticatedActor Actor actor,
            @PathVariable Long id,
            @Valid @RequestBody UpdateAnnouncementAdminDto.Request request
    ) {
        UpdateAnnouncementAdminData.Command command = request.toCommand(actor, id);
        UpdateAnnouncementAdminData.Result result = updateAnnouncementAdminUseCase.execute(command);
        return ApiResponse.success(UpdateAnnouncementAdminDto.Response.from(result), "공지가 성공적으로 수정되었습니다.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAnnouncement(
            @AuthenticatedActor Actor actor,
            @PathVariable Long id
    ) {
        DeleteAnnouncementAdminData.Command command = new DeleteAnnouncementAdminData.Command(actor, id);
        deleteAnnouncementAdminUseCase.execute(command);
        return ApiResponse.success("공지가 성공적으로 삭제되었습니다.");
    }
}
