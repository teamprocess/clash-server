package com.process.clash.adapter.web.auth.controller;

import com.process.clash.adapter.web.auth.docs.controller.TestControllerDocument;
import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController implements TestControllerDocument {

    private final CheckAdminPolicy checkAdminPolicy;

    @PostMapping("/auth")
    public ApiResponse<Void> testApi(@AuthenticatedActor Actor actor) {
        log.info(actor.toString(), "authenticated");
        return ApiResponse.success(null);
    }

    @PostMapping("/admin")
    public ApiResponse<Void> testAdmin(@AuthenticatedActor Actor actor) {
        log.info(actor.toString(), "admin");
        checkAdminPolicy.check(actor);
        return ApiResponse.success(null);
    }
}
