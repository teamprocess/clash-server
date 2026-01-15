package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CheckAdminPolicy checkAdminPolicy;

    @PostMapping("/auth")
    public ApiResponse<Void> testApi(@AuthenticatedActor Actor actor) {
        System.out.println(actor);
        return ApiResponse.success(null);
    }

    @PostMapping("/admin")
    public ApiResponse<Void> testAdmin(@AuthenticatedActor Actor actor) {
        checkAdminPolicy.check(actor);
        return ApiResponse.success(null);
    }
}
