package com.process.clash.adapter.web.auth.electron.controller;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.auth.electron.service.ElectronAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/electron")
@RequiredArgsConstructor
public class ElectronAuthController {

    private final ElectronAuthService electronAuthService;

    /**
     * Electron 앱에서 인증 시작
     * @return 웹 로그인 URL과 state
     */
    @PostMapping("/start")
    public ElectronAuthDto.StartResponse start() {
        return electronAuthService.start();
    }

    /**
     * 웹에서 로그인 처리 후 Deep Link로 리다이렉트
     * @param req 로그인 요청 (username, password, recaptcha 등)
     * @return 302 Redirect to Deep Link
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody ElectronAuthDto.LoginRequest req) {
        String location = electronAuthService.loginAndRedirect(req);
        return ResponseEntity.status(302)
                .header("Location", location)
                .build();
    }

    /**
     * Electron 앱에서 일회성 코드를 세션으로 교환
     * @param req code와 state
     * @param httpRequest 세션 생성을 위한 HttpServletRequest
     * @return 사용자 정보 (세션 쿠키는 Set-Cookie로 자동 전달)
     */
    @PostMapping("/exchange")
    public ElectronAuthDto.ExchangeResponse exchange(
            @Valid @RequestBody ElectronAuthDto.ExchangeRequest req,
            HttpServletRequest httpRequest
    ) {
        return electronAuthService.exchange(req, httpRequest);
    }
}
