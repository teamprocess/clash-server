package com.process.clash.application.health.service;

import com.process.clash.application.health.data.GetHealthStatusData;
import com.process.clash.application.health.data.PingData;
import com.process.clash.application.health.port.in.HealthCheckUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckService implements HealthCheckUseCase {

    private final HealthEndpoint healthEndpoint;

    /**
     * 전체 시스템 헬스 상태 확인
     * 
     * @return 헬스 체크 결과 DTO
     */
    @Override
    public GetHealthStatusData.Result getHealthStatus() {
        try {
            HealthComponent healthComponent = healthEndpoint.health();
            
            if (healthComponent instanceof SystemHealth) {
                SystemHealth systemHealth = (SystemHealth) healthComponent;
                Status status = systemHealth.getStatus();
                
                // 기본 상태
                boolean isHealthy = Status.UP.equals(status);
                return buildHealthResponse(systemHealth, isHealthy);
            } else {
                // SystemHealth가 아닌 경우 기본 응답
                return new GetHealthStatusData.Result("UNKNOWN", null, null, null, null);
            }
        } catch (Exception e) {
            log.error("Error checking health status", e);
            return new GetHealthStatusData.Result("ERROR", null, null, null, e.getMessage());
        }
    }

    /**
     * 헬스 체크 응답 메시지 생성
     * 
     * @param systemHealth SystemHealth 객체
     * @param isHealthy 전체 상태
     * @return 헬스 체크 결과 DTO
     */
    private GetHealthStatusData.Result buildHealthResponse(SystemHealth systemHealth, boolean isHealthy) {
        Map<String, HealthComponent> components = systemHealth.getComponents();
        String db = null;
        String redis = null;
        String app = null;
        if (components != null && !components.isEmpty()) {
            // PostgreSQL 상태
            db = getComponentStatus(components, "db");
            
            // Redis 상태
            redis = getComponentStatus(components, "redis");
            
            // Ping (기본 애플리케이션 상태)
            app = getComponentStatus(components, "ping");
        }

        return new GetHealthStatusData.Result(
                isHealthy ? "UP" : "DOWN",
                db,
                redis,
                app,
                null
        );
    }

    /**
     * 특정 컴포넌트의 상태 추출
     * 
     * @param components 컴포넌트 맵
     * @param componentName 컴포넌트 이름
     * @return 상태 문자열 (UP, DOWN, UNKNOWN)
     */
    private String getComponentStatus(Map<String, HealthComponent> components, String componentName) {
        try {
            if (components.containsKey(componentName)) {
                HealthComponent component = components.get(componentName);
                if (component != null) {
                    return component.getStatus().getCode();
                }
            }
            return "UNKNOWN";
        } catch (Exception e) {
            log.debug("Error getting status for component: {}", componentName, e);
            return "ERROR";
        }
    }

    @Override
    public PingData.Result ping() {
        return new PingData.Result("PONG");
    }
}
