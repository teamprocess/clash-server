package com.process.clash.adapter.web.record.admin.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.admin.docs.AdminRecordControllerDocument;
import com.process.clash.adapter.web.record.admin.dto.CutUserRecordAdminDto;
import com.process.clash.application.record.admin.data.CutUserRecordAdminData;
import com.process.clash.application.record.admin.port.in.CutUserRecordAdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/record")
@RequiredArgsConstructor
public class AdminRecordController implements AdminRecordControllerDocument {

    private final CutUserRecordAdminUseCase cutUserRecordAdminUseCase;

    @PostMapping("/users/{userId}/cut")
    public ApiResponse<CutUserRecordAdminDto.Response> cutUserRecord(
        @PathVariable Long userId
    ) {
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);
        CutUserRecordAdminData.Result result = cutUserRecordAdminUseCase.execute(command);

        return ApiResponse.success(
            CutUserRecordAdminDto.Response.from(result),
            "사용자의 기록을 강제 중단했습니다."
        );
    }
}