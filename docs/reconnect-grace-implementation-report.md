# 재접속 Grace 구현 리포트

## 개요

서버 재배포 또는 일시적인 네트워크 단절로 Socket.IO 연결이 끊겨도, 진행 중인 공부 세션이 즉시 종료되지 않도록 reconnect grace 정책을 추가했다.

이번 구현의 목표는 아래 두 가지였다.

1. 소켓 disconnect 직후 세션을 종료하지 않는다.
2. 사용자가 1분 안에 재접속하지 않으면 `OFFLINE` 으로 확정하고 세션을 종료한다.

## 최종 동작 방식

### 상태 전이

- `ONLINE`
- `AWAY`
- `RECONNECTING`
- `OFFLINE`

동작 규칙:

- 소켓 connect:
  - `RECONNECTING` 또는 `OFFLINE` 상태에서 `ONLINE` 으로 복귀
- `presence:away`:
  - 기존 정책 유지
- 소켓 disconnect:
  - 즉시 `OFFLINE` 으로 내리지 않고 `RECONNECTING` 으로 전이
- reconnect grace 1분 만료:
  - `RECONNECTING -> OFFLINE`
  - 이 시점에만 활성 공부 세션 자동 종료

## 구현 내용

### 1. reconnect 상태 저장소 추가

추가 파일:

- `src/main/java/com/process/clash/application/realtime/port/out/PresenceReconnectStatePort.java`
- `src/main/java/com/process/clash/adapter/persistence/realtime/PresenceReconnectPersistenceAdapter.java`

역할:

- reconnect grace 대상 사용자 저장
- timeout 만료 대상 조회
- 재연결 성공 시 reconnect 대기 상태 제거

저장소는 Redis를 사용한다.

### 2. UserPresenceService 정책 변경

변경 파일:

- `src/main/java/com/process/clash/application/realtime/service/UserPresenceService.java`
- `src/main/java/com/process/clash/application/realtime/data/UserActivityStatus.java`

핵심 변경:

- `RECONNECTING` 상태 추가
- 마지막 연결이 끊기면 즉시 `OFFLINE` 대신 reconnect pending 으로 전환
- `getStatus`, `getStatuses` 가 reconnect pending 사용자를 `RECONNECTING` 으로 해석
- timeout 만료 처리 메서드 추가
- startup recovery 등록 메서드 추가

### 3. timeout / startup recovery 서비스와 스케줄러 추가

추가 파일:

- `src/main/java/com/process/clash/application/realtime/service/PresenceReconnectTimeoutService.java`
- `src/main/java/com/process/clash/application/realtime/service/PresenceReconnectRecoveryService.java`
- `src/main/java/com/process/clash/adapter/scheduler/PresenceReconnectScheduler.java`

설계:

- scheduler 는 10초 고정 지연으로 reconnect timeout 만료를 스캔한다.
- timeout 시 `RECONNECTING -> OFFLINE` 전이를 발생시킨다.
- 기존 `RecordV2PresenceStatusChangedNotifier` 가 이 `OFFLINE` 전이를 받아 세션 종료를 수행한다.
- 서버 startup 시 활성 record 세션 사용자들을 `RECONNECTING` 으로 복구한다.

즉, 서버가 내려가면서 disconnect 이벤트를 정상적으로 처리하지 못해도, 서버가 다시 올라오면 활성 세션 사용자에게 재접속 기회를 한 번 더 준다.

### 4. 설정 추가

변경 파일:

- `src/main/resources/application.yml`
- `src/main/java/com/process/clash/infrastructure/config/SocketIoProperties.java`

추가 설정:

- `realtime.socketio.reconnect-grace-seconds`
  - 기본값 `60`
- `realtime.socketio.reconnect-sweep-fixed-delay-ms`
  - 기본값 `10000`

## 영향 범위

### 공부 세션 종료 정책

변경 전:

- disconnect 즉시 세션 종료

변경 후:

- disconnect 즉시 종료하지 않음
- 1분 내 재접속 시 기존 세션 유지
- 1분 동안 재접속이 없을 때만 종료

### API / 응답 상태값

`UserActivityStatus` 응답에 `RECONNECTING` 이 추가되었다.

영향 받는 응답:

- 내 프로필의 `activityStatus`
- 라이벌 상태의 `status`

클라이언트는 `RECONNECTING` 값을 처리할 수 있어야 한다.

### 기존 notifier 재사용

기존 `RecordV2PresenceStatusChangedNotifier` 구조는 유지했다.

이점:

- 세션 종료 로직 중복 없음
- `OFFLINE` 확정 시점만 바뀌고, 실제 종료 처리와 EXP 지급 흐름은 기존 구조 재사용

## 테스트

실행:

```bash
./gradlew test
```

검증한 시나리오:

- disconnect 직후 `RECONNECTING` 전이
- grace 내 재접속 시 `ONLINE` 복귀
- grace 만료 시 `OFFLINE` 확정
- `DEVELOP` / `TASK` 세션 disconnect 직후 유지
- startup recovery 시 활성 세션 사용자 reconnect 복구
- notifier 가 `RECONNECTING -> OFFLINE` 에서만 세션 종료 수행

## 남은 제약 사항

이번 구현은 **공부 세션 연속성 복구**에 초점을 맞췄다.

현재 제약:

- 전체 presence 상태를 Redis로 완전히 이전한 것은 아니다.
- 서버 재기동 후 활성 공부 세션이 없는 일반 온라인 사용자는 자동으로 `RECONNECTING` 복구되지 않는다.
- 멀티 인스턴스 환경에서의 cross-instance socket broadcast 문제는 이번 범위에 포함하지 않았다.

즉, 이번 변경은 사용자가 제기한 핵심 문제인 "재배포로 인해 진행 중 공부 세션이 강제 종료되는 문제"를 해결하는 데 집중한 구현이다.

## 권장 후속 작업

1. 클라이언트에서 `RECONNECTING` 상태 UI를 명시적으로 처리
2. 재연결 직후 `/api/v2/record/current` 재조회로 세션 UI 복구
3. 필요하면 향후 presence 전체를 Redis 기반으로 확장
4. 멀티 인스턴스 운영 시 Redis pub/sub 기반 실시간 fan-out 검토
