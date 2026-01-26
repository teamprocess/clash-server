package com.process.clash.adapter.web.group.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.group.docs.controller.GroupControllerDocument;
import com.process.clash.adapter.web.group.dto.CreateGroupDto;
import com.process.clash.adapter.web.group.dto.GetAllGroupsDto;
import com.process.clash.adapter.web.group.dto.GetGroupActivityDto;
import com.process.clash.adapter.web.group.dto.GetGroupDetailDto;
import com.process.clash.adapter.web.group.dto.GetMyGroupsDto;
import com.process.clash.adapter.web.group.dto.JoinGroupDto;
import com.process.clash.adapter.web.group.dto.UpdateGroupDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.CreateGroupData;
import com.process.clash.application.group.data.DeleteGroupData;
import com.process.clash.application.group.data.GetAllGroupsData;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.data.GetGroupDetailData;
import com.process.clash.application.group.data.GetMyGroupsData;
import com.process.clash.application.group.data.JoinGroupData;
import com.process.clash.application.group.data.QuitGroupData;
import com.process.clash.application.group.data.UpdateGroupData;
import com.process.clash.application.group.port.in.CreateGroupUseCase;
import com.process.clash.application.group.port.in.DeleteGroupUseCase;
import com.process.clash.application.group.port.in.GetAllGroupsUseCase;
import com.process.clash.application.group.port.in.GetGroupActivityUseCase;
import com.process.clash.application.group.port.in.GetGroupDetailUseCase;
import com.process.clash.application.group.port.in.GetMyGroupsUseCase;
import com.process.clash.application.group.port.in.JoinGroupUseCase;
import com.process.clash.application.group.port.in.QuitGroupUseCase;
import com.process.clash.application.group.port.in.UpdateGroupUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController implements GroupControllerDocument {

    private final GetAllGroupsUseCase getAllGroupsUseCase;
    private final GetMyGroupsUseCase getMyGroupsUseCase;
    private final CreateGroupUseCase createGroupUseCase;
    private final UpdateGroupUseCase updateGroupUseCase;
    private final DeleteGroupUseCase deleteGroupUseCase;
    private final JoinGroupUseCase joinGroupUseCase;
    private final QuitGroupUseCase quitGroupUseCase;
    private final GetGroupActivityUseCase getGroupActivityUseCase;
    private final GetGroupDetailUseCase getGroupDetailUseCase;

    @GetMapping
    public ApiResponse<GetAllGroupsDto.Response> getAllGroups(
        @AuthenticatedActor Actor actor,
        @ModelAttribute GetAllGroupsDto.Request request
    ) {
        GetAllGroupsData.Command command = request.toCommand(actor);
        GetAllGroupsData.Result result = getAllGroupsUseCase.execute(command);
        GetAllGroupsDto.Response response = GetAllGroupsDto.Response.from(result);
        return ApiResponse.success(response, "그룹 목록 조회를 성공했습니다.");
    }

    @GetMapping("/my")
    public ApiResponse<GetMyGroupsDto.Response> getMyGroups(
        @AuthenticatedActor Actor actor,
        @ModelAttribute GetMyGroupsDto.Request request
    ) {
        GetMyGroupsData.Command command = request.toCommand(actor);
        GetMyGroupsData.Result result = getMyGroupsUseCase.execute(command);
        GetMyGroupsDto.Response response = GetMyGroupsDto.Response.from(result);
        return ApiResponse.success(response, "참여한 그룹 목록 조회를 성공했습니다.");
    }

    @GetMapping("/{groupId}")
    public ApiResponse<GetGroupDetailDto.Response> getGroupDetail(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId
    ) {
        GetGroupDetailData.Command command = new GetGroupDetailData.Command(actor, groupId);
        GetGroupDetailData.Result result = getGroupDetailUseCase.execute(command);
        GetGroupDetailDto.Response response = GetGroupDetailDto.Response.from(result);
        return ApiResponse.success(response, "그룹 상세 조회를 성공했습니다.");
    }

    @PostMapping
    public ApiResponse<Void> createGroup(
        @AuthenticatedActor Actor actor,
        @Valid @RequestBody CreateGroupDto.Request request
    ) {
        CreateGroupData.Command command = request.toCommand(actor);
        createGroupUseCase.execute(command);
        return ApiResponse.success("그룹 생성을 성공했습니다.");
    }

    @PatchMapping("/{groupId}")
    public ApiResponse<Void> updateGroup(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId,
        @Valid @RequestBody UpdateGroupDto.Request request
    ) {
        UpdateGroupData.Command command = request.toCommand(actor, groupId);
        updateGroupUseCase.execute(command);
        return ApiResponse.success("그룹 수정을 성공했습니다.");
    }

    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteGroup(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId
    ) {
        DeleteGroupData.Command command = new DeleteGroupData.Command(actor, groupId);
        deleteGroupUseCase.execute(command);
        return ApiResponse.success("그룹 삭제를 성공했습니다.");
    }

    @PostMapping("/{groupId}/join")
    public ApiResponse<Void> joinGroup(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId,
        @RequestBody(required = false) JoinGroupDto.Request request
    ) {
        JoinGroupData.Command command =
            request == null ? new JoinGroupData.Command(actor, groupId, null) : request.toCommand(actor, groupId);
        joinGroupUseCase.execute(command);
        return ApiResponse.success("그룹 참여를 성공했습니다.");
    }

    @PostMapping("/{groupId}/quit")
    public ApiResponse<Void> quitGroup(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId
    ) {
        QuitGroupData.Command command = QuitGroupData.Command.of(actor, groupId);
        quitGroupUseCase.execute(command);
        return ApiResponse.success("그룹 탈퇴를 성공했습니다.");
    }

    @GetMapping("/{groupId}/activity")
    public ApiResponse<GetGroupActivityDto.Response> getGroupActivity(
        @AuthenticatedActor Actor actor,
        @PathVariable Long groupId,
        @ModelAttribute GetGroupActivityDto.Request request
    ) {
        GetGroupActivityData.Command command = GetGroupActivityData.Command.of(actor, groupId, request.page());
        GetGroupActivityData.Result result = getGroupActivityUseCase.execute(command);
        GetGroupActivityDto.Response response = GetGroupActivityDto.Response.from(result);
        return ApiResponse.success(response, "그룹 활동 조회를 성공했습니다.");
    }
}
