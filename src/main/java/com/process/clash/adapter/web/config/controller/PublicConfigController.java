package com.process.clash.adapter.web.config.controller;

import com.process.clash.adapter.web.config.dto.PublicConfigDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class PublicConfigController {

    @Value("${google.recaptcha.key.site}")
    private String recaptchaSiteKey;

    /**
     * 공개 설정 정보 반환
     * - Recaptcha 사이트 키 등 클라이언트에서 필요한 공개 정보
     */
    @GetMapping("/public")
    public PublicConfigDto.Response getPublicConfig() {
        PublicConfigDto.RecaptchaConfig recaptcha = new PublicConfigDto.RecaptchaConfig(recaptchaSiteKey);
        return new PublicConfigDto.Response(recaptcha);
    }
}
