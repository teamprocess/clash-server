## Electron 회원가입 플로우

### 1단계: 회원가입 세션 시작
**엔드포인트:** `POST /api/auth/electron/signup/start`

**헤더:**
```
X-Recaptcha-Token: <recaptcha_token>
```

**처리 과정:**
1. **RecaptchaFilter**: Recaptcha 토큰 검증
2. **ElectronAuthService**:
   - UUID state 생성
   - Redis 저장 (TTL: 5분)
   - 회원가입 URL 생성

**응답:**
```json
{
  "success": true,
  "data": {
    "signupUrl": "https://api.clash.kr/auth-signup.html?state=xxx&redirectUri=clashapp://auth",
    "state": "abc123..."
  }
}
```

---

### 2단계: 웹 페이지에서 회원가입
**엔드포인트:** `POST /api/auth/electron/signup`

**요청:**
```json
{
  "username": "user123",
  "email": "user@example.com",
  "password": "Password123",
  "state": "abc123...",
  "redirectUri": "clashapp://auth"
}
```

**헤더:**
```
X-Recaptcha-Token: <recaptcha_token>
```

**처리 과정:**
1. **RecaptchaFilter**: Recaptcha 토큰 검증
2. **ElectronAuthService**:
   - redirectUri 검증
   - state 검증 (validateState)
   - 유저네임 중복 체크
   - **state 소비** (consumeState)
   - 비밀번호 암호화
   - PendingUser 생성 및 Redis 저장 (state 키, TTL: 10분)
   - 6자리 인증 코드 생성 및 Redis 저장 (state 키, TTL: 5분)
   - SignupSession 저장 (state -> redirectUri 매핑)
   - 이메일 발송
   - **실패 시**: Redis 데이터 롤백 (보상 로직)

**응답:**
```json
{
  "success": true,
  "message": "회원가입 요청이 완료되었습니다. 이메일 인증을 진행해주세요."
}
```

---

### 3단계: 이메일 인증 및 Deep Link
**엔드포인트:** `POST /api/auth/electron/signup/verify-email`

**요청:**
```json
{
  "verificationCode": "123456",
  "state": "abc123...",
  "redirectUri": "clashapp://auth"
}
```

**헤더:**
```
X-Recaptcha-Token: <recaptcha_token>
```

**처리 과정:**
1. **RecaptchaFilter**: Recaptcha 토큰 검증
2. **ElectronAuthService**:
   - redirectUri 검증
   - SignupSession 소비 (state -> redirectUri 조회 및 삭제)
   - PendingUser 조회 (state 키)
   - 인증 코드 검증 (state 키)
   - User DB 저장
   - PendingUser 및 인증 코드 Redis 삭제
   - 일회성 code 생성 및 저장 (TTL: 60초)
   - Deep link URL 생성

**응답:**
```json
{
  "success": true,
  "data": {
    "redirectUrl": "clashapp://auth?code=xyz789&state=abc123"
  }
}
```

---

### 4단계: Code 교환 및 세션 생성
**엔드포인트:** `POST /api/auth/electron/exchange`

**요청:**
```json
{
  "code": "xyz789",
  "state": "abc123..."
}
```

**헤더:**
```
X-Recaptcha-Token: <recaptcha_token>
```

**처리 과정:**
1. **RecaptchaFilter**: Recaptcha 토큰 검증
2. **ElectronAuthService**:
   - 일회성 code 소비 (Redis getAndDelete)
   - state 일치 확인
   - User 조회
   - 세션 생성 (rememberMe: true, 30일)
   - 로그인 이벤트 기록

**응답:**
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "username": "user123",
    "role": "USER"
  },
  "message": "로그인을 성공했습니다."
}
```

---

## 보안 검증

### RecaptchaFilter (모든 인증 전 엔드포인트)

**PROTECTED_PATHS:**
```java
"/api/auth/sign-up"                              // 일반 회원가입
"/api/auth/verify-email"                         // 일반 이메일 인증
"/api/auth/username-duplicate-check"             // 중복 체크

"/api/auth/electron/start"                       // Electron 로그인 시작
"/api/auth/electron/login"                       // Electron 로그인
"/api/auth/electron/exchange"                    // Code 교환
"/api/auth/electron/signup/start"                // Electron 회원가입 시작
"/api/auth/electron/signup"                      // Electron 회원가입
"/api/auth/electron/signup/username-check"       // Electron 중복 체크
"/api/auth/electron/signup/verify-email"         // Electron 이메일 인증
```

**검증 방식:**
- 헤더에서 `X-Recaptcha-Token` 추출
- Google Recaptcha API 호출
- Score 0.5 이상 통과

---

### RateLimitFilter (인증된 사용자만)

**동작:**
```java
// 인증 안 된 사용자는 자동 통과
if (authentication == null || !authentication.isAuthenticated()) {
    chain.doFilter(request, response);
    return;
}

// 인증된 사용자는 userId 기반 Rate Limit 적용
// - USER: 1분당 100회
// - ADMIN: 1분당 1000회
```

**EXCLUDED_PATHS:**
```java
"/api/auth/sign-in"
"/api/auth/sign-up"
"/api/auth/verify-email"
"/api/auth/username-duplicate-check"
"/api/auth/electron/**"
// → 인증 전 엔드포인트는 제외 (Recaptcha로 보호)
```

---

### Validation (DTO 레벨)

**SignUpDto.Request:**
```java
@NotBlank @Size(min=3, max=20) @Pattern(regexp="^[a-zA-Z0-9_-]+$")
String username;

@NotBlank @Email
String email;

@NotBlank @Size(min=8, max=100) @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d).+$")
String password;

@NotBlank @Size(min=1, max=50)
String name;
```

**ElectronAuthDto.SignupRequest:**
```java
@NotBlank @Size(min=3, max=20) @Pattern(regexp="^[a-zA-Z0-9_-]+$")
String username;

@NotBlank @Email
String email;

@NotBlank @Size(min=8, max=100)
String password;

@NotBlank
String state;

@NotBlank
String redirectUri;
```

---

## 데이터 구조

### Redis 캐싱 전략

#### 일반 회원가입
```
Key: signup_token (쿠키로 전달)
├─ PendingUser: {username, email, name, encodedPassword}  [TTL: 30분]
└─ VerificationCode: "123456"                             [TTL: 5분]
```

#### Electron 회원가입
```
Key: state (클라이언트가 유지)
├─ State: "1"                                             [TTL: 5분]
├─ SignupSession: redirectUri                             [TTL: 10분]
├─ PendingUser: {username, email, name, encodedPassword}  [TTL: 10분]
├─ VerificationCode: "123456"                             [TTL: 5분]
└─ OneTimeCode: {state, userId}                           [TTL: 60초]
```

**Key Prefix:**
```
electron:auth:state:{state}       → "1"
electron:auth:signup:{state}      → redirectUri
pending_user:{token}              → PendingUserDto
verification_code:{token}         → "123456"
electron:auth:code:{code}         → OneTimeCodePayload
```

---

### DB 엔티티

**User:**
```java
{
  id: Long,
  username: String,        // unique
  email: String,           // unique
  name: String,
  password: String,        // BCrypt 암호화
  role: Role,              // USER, ADMIN
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

**AuthEvent:**
```java
{
  id: Long,
  username: String,
  ipAddress: String,
  userAgent: String,
  eventType: String,       // "LOGIN", "LOGOUT"
  timestamp: LocalDateTime
}
```

**UserPomodoroSetting:**
```java
{
  id: Long,
  userId: Long,
  workMinutes: Integer,    // default: 25
  shortBreakMinutes: Integer,  // default: 5
  longBreakMinutes: Integer,   // default: 15
  cyclesUntilLongBreak: Integer  // default: 4
}
```

---

## 에러 처리

### 일반 회원가입 예외

| 예외 | HTTP | 메시지 | 발생 시점 |
|------|------|--------|-----------|
| `UsernameAlreadyExistException` | 409 | 이미 존재하는 유저네임 | sign-up |
| `EmailAlreadyExistException` | 409 | 이미 존재하는 이메일 | sign-up |
| `MailDeliveryException` | 500 | 이메일 발송 실패 | sign-up |
| `InvalidVerificationCodeException` | 400 | 잘못된 인증 코드 | verify-email |
| `VerificationCodeExpiredException` | 400 | 인증 코드 만료 | verify-email |
| `PendingUserNotFoundException` | 404 | 임시 사용자 정보 없음 | verify-email |

### Electron 회원가입 예외

| 예외 | HTTP | 메시지 | 발생 시점 |
|------|------|--------|-----------|
| `InvalidRedirectUriException` | 400 | 허용되지 않은 Redirect URI | 모든 단계 |
| `InvalidStateException` | 400 | 유효하지 않은 State | signup, verify-email |
| `RecaptchaVerificationFailedException` | 403 | Recaptcha 검증 실패 | 모든 단계 |
| `UsernameAlreadyExistException` | 409 | 이미 존재하는 유저네임 | signup |
| `InvalidAuthCodeException` | 400 | 잘못된 인증 코드 | verify-email, exchange |
| `StateMismatchException` | 400 | State 불일치 | exchange |
| `UserNotFoundInAuthException` | 404 | 사용자 정보 없음 | exchange |

---

## 시퀀스 다이어그램

### 일반 회원가입
```
Client                RecaptchaFilter    SignUpService      Redis         DB          Email
  |                         |                  |              |            |            |
  |--POST /sign-up--------->|                  |              |            |            |
  | + X-Recaptcha-Token     |                  |              |            |            |
  |                         |--verify--------->|              |            |            |
  |                         |<-----------------OK             |            |            |
  |                         |                  |              |            |            |
  |                         |----------------->|              |            |            |
  |                         |                  |--check------>|            |            |
  |                         |                  |<-------------|            |            |
  |                         |                  |              |            |            |
  |                         |                  |--save------->|            |            |
  |                         |                  |              |            |            |
  |                         |                  |--send---------------------->|          |
  |                         |                  |              |            |            |
  |<------------------------OK + Cookie        |              |            |            |
  |                         |                  |              |            |            |
  |--POST /verify-email---->|                  |              |            |            |
  | + Cookie                |--verify--------->|              |            |            |
  |                         |<-----------------OK             |            |            |
  |                         |                  |              |            |            |
  |                         |----------------->|              |            |            |
  |                         |                  |--verify----->|            |            |
  |                         |                  |<-------------|            |            |
  |                         |                  |              |            |            |
  |                         |                  |--save--------|----------->|            |
  |                         |                  |              |            |            |
  |                         |                  |--delete----->|            |            |
  |<------------------------OK                 |              |            |            |
```

### Electron 회원가입
```
Electron   Web        RecaptchaFilter    ElectronAuthService    Redis         DB
  |         |                |                   |                |            |
  |--POST /signup/start----->|                   |                |            |
  |         |                |--verify---------->|                |            |
  |         |                |<------------------OK               |            |
  |         |                |                   |                |            |
  |         |                |------------------>|                |            |
  |         |                |                   |--save--------->|            |
  |<--------URL + state------|                   |                |            |
  |         |                |                   |                |            |
  |--open browser----------->|                   |                |            |
  |         |                |                   |                |            |
  |         |--POST /signup->|                   |                |            |
  |         |                |--verify---------->|                |            |
  |         |                |<------------------OK               |            |
  |         |                |                   |                |            |
  |         |                |------------------>|                |            |
  |         |                |                   |--consume------>|            |
  |         |                |                   |--save--------->|            |
  |         |                |                   |--email-------->|            |
  |         |<---------------OK                  |                |            |
  |         |                |                   |                |            |
  |         |--POST /verify-email--------------->|                |            |
  |         |                |--verify---------->|                |            |
  |         |                |<------------------OK               |            |
  |         |                |                   |                |            |
  |         |                |------------------>|                |            |
  |         |                |                   |--verify------->|            |
  |         |                |                   |--save----------|----------->|
  |         |                |                   |--code--------->|            |
  |         |<---------------deep link           |                |            |
  |<--------redirect---------|                   |                |            |
  |         |                |                   |                |            |
  |--POST /exchange--------->|                   |                |            |
  |         |                |--verify---------->|                |            |
  |         |                |<------------------OK               |            |
  |         |                |                   |                |            |
  |         |                |------------------>|                |            |
  |         |                |                   |--consume------>|            |
  |         |                |                   |--session------>|            |
  |<--------user info--------|                   |                |            |
```

---

## 설정

### application.yml
```yaml
electron:
  auth:
    allowed-redirect-uris:
      - "clashapp://auth"
    auth-web-url: ${ELECTRON_AUTH_WEB_URL:https://api.clash.kr/auth-login.html}
    signup-web-url: ${ELECTRON_SIGNUP_WEB_URL:https://api.clash.kr/auth-signup.html}

google:
  recaptcha:
    key:
      site: ${RECAPTCHA_SITE_KEY}
      secret: ${RECAPTCHA_SECRET_KEY}
      url: https://www.google.com/recaptcha/api/siteverify

spring:
  mail:
    host: email-smtp.ap-northeast-2.amazonaws.com
    port: 587
    username: ${AWS_SES_SMTP_USERNAME}
    password: ${AWS_SES_SMTP_PASSWORD}
```

---

## 프론트엔드 연동

### 일반 회원가입

```javascript
// 1. 회원가입 요청
const response = await fetch('/api/auth/sign-up', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Recaptcha-Token': recaptchaToken  // Recaptcha 토큰
  },
  body: JSON.stringify({
    username: 'user123',
    email: 'user@example.com',
    password: 'Password123',
    name: '홍길동'
  }),
  credentials: 'include'  // 쿠키 포함
});

// 2. 이메일 인증
const verifyResponse = await fetch('/api/auth/verify-email', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Recaptcha-Token': recaptchaToken
  },
  body: JSON.stringify({
    verificationCode: '123456'
  }),
  credentials: 'include'  // signup_token 쿠키 자동 전송
});
```

### Electron 회원가입

```javascript
// 1. 회원가입 시작 (Electron 앱)
const startResponse = await fetch('/api/auth/electron/signup/start', {
  method: 'POST',
  headers: {
    'X-Recaptcha-Token': recaptchaToken
  }
});
const { signupUrl, state } = await startResponse.json();

// 2. 브라우저 열기
shell.openExternal(signupUrl);

// 3. 웹 페이지에서 회원가입 (브라우저)
const signupResponse = await fetch('/api/auth/electron/signup', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Recaptcha-Token': recaptchaToken
  },
  body: JSON.stringify({
    username: 'user123',
    email: 'user@example.com',
    password: 'Password123',
    state: state,
    redirectUri: 'clashapp://auth'
  })
});

// 4. 이메일 인증 (브라우저)
const verifyResponse = await fetch('/api/auth/electron/signup/verify-email', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Recaptcha-Token': recaptchaToken
  },
  body: JSON.stringify({
    verificationCode: '123456',
    state: state,
    redirectUri: 'clashapp://auth'
  })
});
const { redirectUrl } = await verifyResponse.json();

// 5. Deep Link로 앱 복귀
window.location.href = redirectUrl;  // clashapp://auth?code=xxx&state=xxx

// 6. Code 교환 (Electron 앱)
const exchangeResponse = await fetch('/api/auth/electron/exchange', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Recaptcha-Token': recaptchaToken
  },
  body: JSON.stringify({
    code: code,
    state: state
  }),
  credentials: 'include'  // 세션 쿠키 저장
});
const user = await exchangeResponse.json();
```

---

## 테스트 체크리스트

### 기능 테스트
- [ ] 일반 회원가입 성공
- [ ] Electron 회원가입 성공
- [ ] 유저네임 중복 체크
- [ ] 이메일 중복 체크
- [ ] 이메일 발송 확인
- [ ] 인증 코드 검증
- [ ] 세션 생성 확인

### 보안 테스트
- [ ] Recaptcha 없이 요청 시 차단
- [ ] 잘못된 Recaptcha 토큰 차단
- [ ] 만료된 state 차단
- [ ] 잘못된 redirectUri 차단
- [ ] 인증 코드 brute force 방지
- [ ] 일회성 code 재사용 방지
- [ ] Rate Limit 동작 확인

### 예외 처리
- [ ] 유저네임 중복 시 409 응답
- [ ] 이메일 중복 시 409 응답
- [ ] 잘못된 인증 코드 시 400 응답
- [ ] 만료된 토큰 시 400 응답
- [ ] 이메일 발송 실패 시 롤백

---

## 문서 버전
- **작성일**: 2026-02-03
- **버전**: 1.0
- **작성자**: Claude Sonnet 4.5
