package com.process.clash.adapter.google;

import com.process.clash.application.google.port.out.RecaptchaPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
            String url = String.format("%s?secret=%s&response=%s", verifyUrl, secretKey, token);
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

            if (response == null) {
                log.error("Recaptcha response is null");
                return false;
            }

            Boolean success = (Boolean) response.get("success");
            if (success == null || !success) {
                log.warn("Recaptcha verification failed: {}", response.get("error-codes"));
                return false;
            }

            Object scoreObj = response.get("score");
            if (scoreObj == null) {
                log.warn("Recaptcha score is null");
                return false;
            }

            double score = ((Number) scoreObj).doubleValue();
            boolean isValid = score >= MIN_SCORE;

            if (!isValid) {
                log.warn("Recaptcha score too low: {}", score);
            }

            return isValid;

        } catch (RestClientException e) {
            log.error("Failed to verify recaptcha token", e);
            return false;
        } catch (ClassCastException e) {
            log.error("Failed to parse recaptcha response", e);
            return false;
        }
    }
}
