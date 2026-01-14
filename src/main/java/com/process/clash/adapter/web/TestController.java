package com.process.clash.adapter.web;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.infrastructure.principle.AuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/auth")
    public ApiResponse<Void> testApi(@AuthenticationPrincipal AuthUser authUser) {
        System.out.println(authUser);
        return ApiResponse.success(null);
    }
}
