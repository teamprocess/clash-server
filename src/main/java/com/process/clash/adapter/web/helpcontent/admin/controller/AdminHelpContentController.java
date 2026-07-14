package com.process.clash.adapter.web.helpcontent.admin.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.helpcontent.docs.controller.AdminHelpContentControllerDocument;
import com.process.clash.adapter.web.helpcontent.dto.UpdateHelpContentDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.helpcontent.data.UpdateHelpContentData;
import com.process.clash.application.helpcontent.port.in.UpdateHelpContentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/help-contents")
@RequiredArgsConstructor
public class AdminHelpContentController implements AdminHelpContentControllerDocument {

    private final UpdateHelpContentUseCase updateHelpContentUseCase;

    @Override
    @PutMapping("/{key}")
    public ApiResponse<UpdateHelpContentDto.Response> updateHelpContent(
            @AuthenticatedActor Actor actor,
            @PathVariable String key,
            @Valid @RequestBody UpdateHelpContentDto.Request request
    ) {
        UpdateHelpContentData.Result result = updateHelpContentUseCase.execute(request.toCommand(actor, key));
        return ApiResponse.success(
                UpdateHelpContentDto.Response.from(result),
                "도움말 내용이 성공적으로 수정되었습니다."
        );
    }
}
