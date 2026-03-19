# 서버 재배포 시 공부 세션 유지 가이드

## 결론

가능하다. 다만 "소켓 연결 자체를 유지"하는 것은 불가능하고, 대신 **소켓은 다시 연결하되 공부 세션은 끊기지 않게 유지**하는 구조로 바꿔야 한다.

현재 코드베이스는 이미 공부 세션을 DB에 저장하고 있으므로, 구조적으로는 충분히 전환 가능하다. 문제는 세션이 DB에 저장돼 있음에도 불구하고, **소켓 disconnect를 곧바로 세션 종료로 해석하는 현재 presence 설계**에 있다.

## 현재 구조에서 끊기는 이유

### 1. 소켓 연결이 끊기면 presence가 즉시 OFFLINE으로 전이된다

- `src/main/java/com/process/clash/adapter/realtime/socketio/SocketIoConnectionHandler.java`
  - connect 시 `reportUserPresenceUseCase.connected(...)`
  - disconnect 시 `reportUserPresenceUseCase.disconnected(...)`
- `src/main/java/com/process/clash/application/realtime/service/UserPresenceService.java`
  - connectionId 기준으로 메모리 맵을 관리한다.
  - 이 상태는 JVM 메모리에만 존재한다.

즉, 서버가 재배포되면:

1. 기존 JVM 메모리가 사라진다.
2. 소켓 연결이 끊긴다.
3. disconnect 이벤트가 발생한다.
4. presence가 OFFLINE으로 바뀐다.

### 2. OFFLINE/AWAY 전이가 활성 세션 종료를 유발한다

- `src/main/java/com/process/clash/application/record/v2/realtime/RecordV2PresenceStatusChangedNotifier.java`
  - `AWAY` 또는 `OFFLINE` 이 되면 활성 세션을 종료한다.
  - `DEVELOP` 세션은 `AWAY`, `OFFLINE` 둘 다 종료 대상이다.
  - `TASK` 세션은 `OFFLINE`일 때 종료된다.

즉, 현재는 **소켓 transport 상태**와 **공부 세션의 생명주기**가 너무 강하게 결합돼 있다.

### 3. presence 상태 자체가 인메모리라 재배포 내성이 없다

- `UserPresenceService` 는 `sessionByConnectionId`, `counterByUserId` 를 `HashMap` 으로 들고 있다.
- 서버 재시작 시 이 정보는 모두 유실된다.

반면, 공부 세션 자체는 DB에 저장된다.

- `src/main/java/com/process/clash/domain/record/v2/entity/RecordSessionV2.java`
- `src/main/java/com/process/clash/adapter/persistence/record/v2/session/RecordSessionV2PersistenceAdapter.java`

즉, **도메인 세션은 영속적**인데, **presence는 비영속적**이고, 이 둘이 잘못 연결돼 있다.

## 현재 구조에서 이미 좋은 점

완전히 새로 설계할 필요는 없다. 아래 기반은 이미 갖춰져 있다.

### 1. 활성 공부 세션은 DB에 남아 있다

- `/api/v2/record/current` API 존재
- `src/main/java/com/process/clash/application/record/v2/service/GetCurrentRecordV2Service.java`
- `src/main/java/com/process/clash/adapter/web/record/v2/controller/RecordV2Controller.java`

즉, 재연결 후 클라이언트가 현재 세션을 다시 조회해서 UI를 복구할 수 있다.

### 2. HTTP 로그인 세션과 소켓 토큰은 Redis 기반이다

- `build.gradle`
  - `spring-session-data-redis`
- `src/main/resources/application.yml`
  - `spring.session.redis.repository-type: indexed`
- `src/main/java/com/process/clash/adapter/persistence/realtime/SocketTokenPersistenceAdapter.java`
  - 소켓 토큰 저장소가 Redis 기반

즉, 서버 재배포 후에도 사용자가 HTTP 인증을 유지할 가능성이 높고, 필요하면 새 소켓 토큰을 다시 발급받아 재연결할 수 있다.

## 핵심 설계 원칙

### 원칙 1. 공부 세션과 소켓 연결을 분리한다

소켓은 "실시간 통신 채널"일 뿐이다. 공부 세션의 진짜 상태는 DB가 들고 있어야 한다.

즉:

- 소켓 연결이 잠깐 끊겨도 공부 세션은 유지
- 클라이언트가 다시 연결되면 현재 세션을 조회하고 복구
- 정말 오래 끊겼을 때만 세션 종료

### 원칙 2. disconnect 즉시 종료 대신 reconnect grace period를 둔다

권장 상태 전이는 아래와 같다.

- `ONLINE`
- `AWAY`
- `RECONNECTING`
- `OFFLINE`

여기서 중요한 점:

- `presence:away` 는 사용자의 명시적 자리비움 신호
- `disconnect` 는 네트워크/재배포/프로세스 재시작일 수 있으므로 곧바로 OFFLINE으로 보면 안 됨

권장 규칙:

- `ONLINE -> AWAY`
  - 현재 정책 유지 가능
- `ONLINE -> RECONNECTING`
  - 소켓 disconnect 시 진입
  - 아직 세션 종료하지 않음
- `RECONNECTING -> ONLINE`
  - grace window 내 재연결 성공
  - 기존 세션 계속 사용
- `RECONNECTING -> OFFLINE`
  - grace window 만료
  - 이때 세션 종료

## 권장 구현 방향

### 1. Presence 저장소를 인메모리에서 Redis로 이동

가장 중요한 변경이다.

현재 `UserPresenceService` 는 JVM 메모리를 사용하므로 재배포 시 상태가 증발한다. 이를 Redis 기반 저장소로 바꿔야 한다.

권장 저장 정보 예시:

- `presence:user:{userId}`
  - `status`
  - `graceUntil`
  - `lastSeenAt`
- `presence:conn:{connectionId}`
  - `userId`
  - `status`
  - `lastHeartbeatAt`

Redis를 쓰는 이유:

- 재배포 후에도 상태 보존
- 여러 인스턴스 환경으로 확장 가능
- TTL 기반 grace 관리가 쉬움

### 2. disconnect 시 즉시 OFFLINE 처리하지 말고 RECONNECTING 처리

변경 대상:

- `src/main/java/com/process/clash/adapter/realtime/socketio/SocketIoConnectionHandler.java`
- `src/main/java/com/process/clash/application/realtime/service/UserPresenceService.java`

권장 변경:

- `onDisconnect()` 에서 `disconnected(connectionId)` 대신
  - `disconnectedWithGrace(connectionId, now + graceWindow)` 같은 의미로 변경
- grace window 예시:
  - 60초
  - 모바일/데스크톱 환경이면 90초 정도도 가능

중요한 점:

- 서버 재배포나 순간 네트워크 단절은 대부분 수 초에서 수십 초 수준
- 이 시간 동안 세션을 열어두면 체감상 "이어지는 것처럼" 보인다

### 3. 세션 종료 트리거를 OFFLINE 확정 시점으로 이동

현재는 `RecordV2PresenceStatusChangedNotifier` 가 `AWAY` 또는 `OFFLINE` 전이만 보면 곧바로 세션을 종료한다.

이를 다음처럼 바꾸는 것이 좋다.

- `AWAY`
  - 현재 정책 유지 가능
  - 특히 `DEVELOP` 세션은 즉시 종료 유지 가능
- `RECONNECTING`
  - 종료하지 않음
- `OFFLINE`
  - grace 만료 후 확정된 오프라인일 때만 종료

즉, notifier 조건을 아래처럼 바꾼다.

- 종료 대상:
  - `RECONNECTING -> OFFLINE`
  - 또는 `ONLINE/AWAY -> OFFLINE`
- 종료 제외:
  - `ONLINE -> RECONNECTING`

### 4. grace 만료를 처리하는 별도 scheduler/service 추가

현재도 만료 세션 처리 패턴이 이미 있다.

- `src/main/java/com/process/clash/application/record/v2/service/RecordV2TaskSessionTimeoutService.java`
- `src/main/java/com/process/clash/adapter/scheduler/RecordV2TaskSessionTimeoutScheduler.java`

같은 방식으로 아래를 추가하면 된다.

- `PresenceReconnectTimeoutService`
- `PresenceReconnectTimeoutScheduler`

역할:

1. Redis에서 grace 만료된 사용자 조회
2. 해당 사용자를 `OFFLINE` 으로 확정
3. 활성 공부 세션이 있으면 종료
4. 재배포 직후 `ApplicationReadyEvent` 에서도 한 번 실행

이렇게 해야 서버가 grace 도중 죽었다 다시 떠도, 만료 처리가 누락되지 않는다.

### 5. 클라이언트는 재연결 후 현재 세션을 재조회해서 복구

서버 변경만으로는 UX가 완성되지 않는다. 클라이언트도 아래 흐름을 가져야 한다.

1. 소켓 disconnect 감지
2. 자동 reconnect 시도
3. reconnect 성공 후 새 소켓 토큰이 필요하면 `/api/realtime/socket/token` 재호출
4. reconnect 직후 `/api/v2/record/current` 호출
5. 활성 세션이 있으면 UI 타이머/상태 복구
6. `presence:online` 재전송

이 흐름을 쓰면 서버 재배포가 있어도 사용자는 "세션이 살아있다"고 느낄 수 있다.

## DEVELOP 세션에서 추가로 고려할 점

이 프로젝트는 DEVELOP 세션에서 앱 전환 이력을 별도 segment 로 저장한다.

- `src/main/java/com/process/clash/domain/record/v2/entity/RecordDevelopSessionSegmentV2.java`
- `src/main/java/com/process/clash/application/record/v2/service/SwitchDevelopAppV2Service.java`

여기서 중요한 한계가 있다.

### 서버 다운 중에는 앱 전환 이벤트를 실시간으로 받을 수 없다

즉, 아래는 자동으로 복구되지 않는다.

- 서버가 죽어 있는 동안 사용자가 `VSCode -> IntelliJ` 로 앱 전환한 사실

따라서 선택지가 두 가지다.

### 옵션 A. 단순 복구형

재연결 후 현재 앱만 다시 서버에 반영한다.

흐름:

1. 클라이언트 재연결
2. `/api/v2/record/current` 로 현재 DEVELOP 세션 조회
3. 로컬에서 현재 활성 앱 확인
4. 서버의 `appId` 와 다르면 `/api/v2/record/activities/switch-app` 호출

장점:

- 구현이 가장 단순
- 대부분의 UX 문제 해결

단점:

- 서버 다운 구간 중간의 세부 앱 전환 이력은 잃는다

### 옵션 B. 정확도 보존형

클라이언트가 로컬에서 앱 전환 이벤트를 버퍼링하고, 재연결 후 서버에 일괄 업로드한다.

필요한 추가 요소:

- 클라이언트 로컬 event queue
- 이벤트 id / occurredAt 포함
- 서버 batch sync API
- 중복 처리용 idempotency

장점:

- 서버 다운 중 앱 전환 히스토리까지 복구 가능

단점:

- 구현 난이도 높음

실무적으로는 **먼저 옵션 A로 가고**, 추후 정확도가 중요해지면 옵션 B를 붙이는 것이 현실적이다.

## 최소 변경안과 권장안 비교

### 최소 변경안

핵심만 바꾸는 방법이다.

- disconnect 즉시 세션 종료 제거
- `OFFLINE` 확정 전에 60초 grace 도입
- 재연결 후 `/api/v2/record/current` 로 UI 복구

장점:

- 구현량이 적다
- 현재 문제를 빠르게 완화할 수 있다

단점:

- presence가 여전히 인메모리면 재배포 안정성이 불완전하다
- 멀티 인스턴스 확장성도 부족하다

즉, **재배포 문제를 진짜 해결하려면 최소한 presence 상태는 Redis로 빼는 것이 좋다.**

### 권장안

- presence 저장소 Redis화
- `RECONNECTING` 상태 추가
- grace 만료 scheduler 추가
- reconnect 후 current record 재조회
- DEVELOP 세션 current app 재동기화

이 방향이 이번 요구사항에 가장 잘 맞는다.

## 멀티 인스턴스 환경이라면 추가로 봐야 할 부분

현재 소켓 broadcast 는 로컬 `SocketIOServer` 에만 보낸다.

- `src/main/java/com/process/clash/adapter/realtime/socketio/SocketIoBroadcastAdapter.java`

즉, 인스턴스가 여러 개면 다음 문제가 생길 수 있다.

- HTTP 요청은 A 인스턴스로 감
- 소켓 연결은 B 인스턴스에 붙어 있음
- A에서 보낸 broadcast가 B의 소켓 클라이언트에 전달되지 않음

만약 운영이 단일 인스턴스면 당장 큰 문제는 아닐 수 있다. 하지만 롤링 배포나 다중 인스턴스를 염두에 둔다면 아래 중 하나가 필요하다.

- 로드밸런서 sticky routing
- Redis pub/sub 기반 fan-out
- 각 인스턴스가 동일 이벤트를 구독해 로컬 socket room으로 재방송

이번 "재배포 후 재연결" 요구사항 자체는 이것 없이도 구현할 수 있지만, **운영 구조가 다중 인스턴스라면 같이 검토해야 한다.**

## 추천 구현 순서

### 1단계. 세션 종료와 disconnect 분리

- `UserActivityStatus` 에 `RECONNECTING` 추가
- disconnect 시 즉시 OFFLINE으로 내리지 않기
- `RecordV2PresenceStatusChangedNotifier` 에서 `RECONNECTING` 은 종료 조건에서 제외

### 2단계. presence 저장소 Redis화

- `UserPresenceService` 책임 분리
- 인메모리 맵 대신 Redis adapter 도입
- connection, user status, graceUntil 저장

### 3단계. grace 만료 scheduler 추가

- 만료 사용자 OFFLINE 확정
- 세션 자동 종료
- startup 시 sweep 실행

### 4단계. 클라이언트 resume flow 적용

- 소켓 재연결
- 새 토큰 발급
- `/api/v2/record/current` 재조회
- `presence:online` 재전송
- DEVELOP 세션이면 현재 앱 재동기화

### 5단계. 필요 시 segment 보강

- 다운타임 중 앱 전환 이력까지 복구하려면 클라이언트 버퍼 + batch sync 도입

## 테스트 시나리오

반드시 아래를 자동화 테스트로 보강하는 것이 좋다.

### 서버 측 테스트

1. `ONLINE -> RECONNECTING` 전이 시 세션이 종료되지 않는다
2. grace 내 재연결하면 세션이 유지된다
3. grace 만료 시 `OFFLINE` 확정 후 세션이 종료된다
4. `AWAY` 는 기존 정책대로 동작한다
5. 서버 재시작 후 startup sweep 이 만료 grace 를 처리한다

### 통합 테스트

1. 세션 시작 후 disconnect
2. 30초 내 reconnect
3. `/api/v2/record/current` 에 기존 세션이 남아 있다
4. stop 호출 시 기존 세션이 정상 종료된다

### 클라이언트 연동 테스트

1. 서버 재배포
2. 소켓 자동 재연결
3. current record 재조회
4. 타이머 UI가 이어진다
5. DEVELOP 세션이면 현재 앱 상태가 다시 동기화된다

## 이번 요구사항에 대한 현실적인 권장안

이번 요구사항 기준으로는 아래 조합을 추천한다.

1. 공부 세션 종료를 raw socket disconnect 와 분리
2. disconnect 는 60~90초의 reconnect grace 로 처리
3. presence 상태는 Redis로 이동
4. reconnect 후 `/api/v2/record/current` 로 세션 복구
5. DEVELOP 세션은 재연결 직후 현재 앱을 다시 동기화

이 정도면:

- 서버 재배포
- 짧은 네트워크 단절
- 소켓 재연결

같은 상황에서 세션이 강제 종료되지 않게 만들 수 있다.

## 구조적으로 불가능한 것

아래는 구분해서 봐야 한다.

### 가능한 것

- 서버가 잠깐 내려갔다 올라와도 공부 세션을 이어가기
- 재연결 후 UI 복구하기
- 세션 시작 시각을 유지한 채 계속 진행하기

### 불가능하거나 추가 설계가 필요한 것

- TCP/WebSocket 연결 자체를 재배포 중에도 끊기지 않게 하기
- 서버 다운 중 발생한 모든 실시간 앱 전환 이벤트를 자동으로 정확히 복원하기

즉, 목표는 "소켓 연결 보존"이 아니라 **"도메인 세션 복원"** 으로 잡아야 한다.
