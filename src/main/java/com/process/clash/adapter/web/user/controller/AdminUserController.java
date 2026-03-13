package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.application.user.user.port.in.ForceLogoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final ForceLogoutUseCase forceLogoutUseCase;

    @DeleteMapping("/{username}/sessions")
    public ApiResponse<Void> forceLogout(@PathVariable String username) {
        forceLogoutUseCase.execute(username);
        return ApiResponse.success(String.format("%s의 세션 및 자동 로그인 정보가 만료되었습니다.", username));
    }
}
