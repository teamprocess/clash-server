<p>
  <img src="docs/assets/clash-background.svg" width="100%" alt="Clash" />
</p>

<h1>
  <img src="./docs/assets/readme-title.svg" alt="Clash Server" />
</h1>

**학습 기록 기반 경쟁 애플리케이션, Clash**

Clash Server는 IDE 사용 시간과 GitHub 활동을 기반으로 학습 기록을 수집하고, 경쟁·로드맵·보상·알림 기능을 제공하는 Spring Boot API 서버입니다.

이 저장소는 Clash의 인증, 유저, 기록, 경쟁, 로드맵, 상점, 실시간 동기화 도메인을 관리합니다.

## Service

- **학습 기록**: 사용자의 개발 시간, 할 일, GitHub 활동을 날짜 기준으로 저장하고 조회합니다.
- **경쟁**: 개인 랭킹, 라이벌, 배틀, 그룹 데이터를 통해 학습 기록을 비교 가능한 지표로 제공합니다.
- **로드맵**: 전공, 섹션, 챕터, 미션 진행 상태를 관리합니다.
- **보상**: 출석, 랭킹, 활동 기록을 기반으로 경험치와 재화를 지급하고 상점 구매를 처리합니다.
- **실시간 동기화**: Socket.IO를 통해 클라이언트의 상태 변경, 알림, 리패치 이벤트를 전달합니다.

## Product Surface

| 영역 | 설명 |
| --- | --- |
| REST API | Clash macOS 앱과 웹 인증 화면에서 사용하는 HTTP API |
| Realtime Server | Socket.IO 기반 presence, 알림, refetch 이벤트 서버 |
| Scheduler | GitHub 통계 동기화, 랭킹 보상, 배틀/시즌 종료, 출석 초기화 작업 |
| Persistence | PostgreSQL, Redis, Flyway 기반 데이터 저장과 마이그레이션 |
| External Integrations | GitHub GraphQL/OAuth, Google reCAPTCHA, AWS S3, AWS SES |

## Features

- **Auth**: 회원가입, 로그인, 세션, reCAPTCHA, Electron 딥링크 인증
- **User/Profile**: 사용자 프로필, GitHub 연동, 아이템 장착, 프로필 이미지 업로드
- **Record**: IDE 사용 시간 기반 학습 기록과 태스크 세션 관리
- **GitHub Sync**: GitHub 활동 통계 수집과 일별 기여 데이터 집계
- **Competition**: 개인 분석, 랭킹, 라이벌, 배틀 기능
- **Group**: 그룹 생성, 가입, 탈퇴, 그룹 활동 조회
- **Roadmap**: 전공 선택, 섹션, 챕터, 미션 진행 관리
- **Shop**: 상품 목록, 추천 상품, 구매, 시즌 상품 관리
- **Notice & Announcement**: 사용자 알림과 서비스 공지
- **Realtime**: socket token 발급, presence 변경, 도메인별 refetch 이벤트 발행

## Tech Stack

| 분류 | 기술 |
| --- | --- |
| Core | Java 21, Spring Boot 3.5.9 |
| Web | Spring MVC, Spring Security, Spring Validation |
| Persistence | Spring Data JPA, PostgreSQL, Flyway |
| Session & Cache | Redis, Spring Session |
| Realtime | netty-socketio |
| External API | Spring WebFlux, GitHub GraphQL/OAuth, Google reCAPTCHA |
| Storage & Mail | AWS S3, AWS SES SMTP |
| API Docs | SpringDoc OpenAPI |
| Build & Deploy | Gradle, Docker, GitHub Actions |

## Architecture

Clash Server는 Hexagonal Architecture를 기준으로 도메인, 유스케이스, 어댑터, 인프라를 분리합니다.

```text
src/main/java/com/process/clash
├── domain             # 순수 도메인 모델과 정책
├── application        # 유스케이스, 서비스, 포트, Command/Result 데이터
├── adapter            # web, persistence, realtime, scheduler, external adapter
└── infrastructure     # security, config, web, 공통 기술 설정
```

의존성은 아래 방향을 유지합니다.

```text
adapter -> application -> domain
infrastructure -> application / adapter support
```

UseCase 접근은 `port`를 이용한 간접적인 접근을 지향합니다.

```text
Client
  ├─ REST API
  └─ Socket.IO
       ↓
adapter
  ├─ web
  ├─ realtime
  ├─ persistence
  ├─ github / google / aws / email
  └─ scheduler
       ↓
application
       ↓
domain
```

## Repository Structure

```text
clash-server
├── src/main/java/com/process/clash
│   ├── domain
│   ├── application
│   ├── adapter
│   └── infrastructure
├── src/main/resources
│   ├── application.yml
│   ├── db/migration
│   ├── graphql/github
│   └── templates/email
├── src/test
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── docs/assets
```
