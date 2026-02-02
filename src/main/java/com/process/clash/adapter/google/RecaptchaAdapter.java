package com.process.clash.adapter.google;

import com.process.clash.application.google.port.out.RecaptchaPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecaptchaAdapter implements RecaptchaPort {

    private final RestTemplate restTemplate;

    @Value("${google.recaptcha.key.secret}")
    private String secretKey;

    @Value("${google.recaptcha.key.url}")
    private String verifyUrl;

    private static final double MIN_SCORE = 0.5;

    @Override
    public boolean verifyToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Recaptcha token is null or blank");
            return false;
        }

        try {
            // POST 본문에 파라미터 담기
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", secretKey);
            params.add("response", token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            RecaptchaResponse response = restTemplate.postForObject(verifyUrl, request, RecaptchaResponse.class);

            if (response == null) {
                log.error("Recaptcha response is null");
                return false;
            }

            if (!response.success()) {
                log.warn("Recaptcha verification failed: {}", response.errorCodes());
                return false;
            }

            if (response.score() == null) {
                log.warn("Recaptcha score is null");
                return false;
            }

            double score = response.score();
            boolean isValid = score >= MIN_SCORE;

            if (!isValid) {
                log.warn("Recaptcha score too low: {}", score);
            }

            return isValid;

        } catch (RestClientException e) {
            log.error("Failed to verify recaptcha token", e);
            return false;
        }
    }
}
