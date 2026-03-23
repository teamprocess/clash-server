package com.process.clash.adapter.web.controller;

import com.process.clash.adapter.web.controller.docs.controller.HealthCheckControllerDocument;
import com.process.clash.adapter.web.controller.dto.HealthResponse;
import com.process.clash.adapter.web.controller.dto.PingResponse;
import com.process.clash.application.health.data.GetHealthStatusData;
import com.process.clash.application.health.data.PingData;
import com.process.clash.application.health.port.in.HealthCheckUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheckController implements HealthCheckControllerDocument {

    private final HealthCheckUseCase healthCheckUseCase;

    /**
     * 간단한 Ping 체크
     * 
     * @return PONG 응답
     */
    @GetMapping("/ping")
    public ResponseEntity<PingResponse> ping() {
        PingData.Result result = healthCheckUseCase.ping();
        PingResponse response = new PingResponse(result.message());
        return ResponseEntity.ok(response);
    }

    /**
     * 상세 헬스 체크
     * DB, Redis, App 상태 포함
     * 
     * @return 상세 헬스 상태
     */
    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        GetHealthStatusData.Result result = healthCheckUseCase.getHealthStatus();
        HealthResponse response = new HealthResponse(
                result.status(),
                result.db(),
                result.redis(),
                result.app(),
                result.message()
        );
        
        // status가 DOWN이면 503 응답
        if ("DOWN".equals(response.status())) {
            return ResponseEntity.status(503).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
