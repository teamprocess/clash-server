# CLASH 서버 개발 가이드

> CLASH 프로젝트를 이어받을 개발자를 위한 종합 개발 가이드

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [아키텍처](#아키텍처)
4. [패키지 구조](#패키지-구조)
5. [코딩 컨벤션](#코딩-컨벤션)
6. [커밋 메시지 컨벤션](#커밋-메시지-컨벤션)
7. [주요 패턴](#주요-패턴)

---

## 프로젝트 개요

**CLASH**는 학습 관리 및 경쟁 시스템을 제공하는 백엔드 서버입니다.

- **License**: AGPL-3.0-with-Commons-Clause
- **Java Version**: 21
- **Spring Boot Version**: 3.5.9

---

## 기술 스택

### Core
- **Java 21**
- **Spring Boot 3.5.9**
- **Spring Data JPA**
- **Spring Security**
- **Spring WebFlux** (외부 API 호출용)

### Database
- **PostgreSQL** (Production)
- **H2** (Test)
- **Redis** (캐싱 및 세션)

### Documentation
- **SpringDoc OpenAPI 3** (Swagger)

### Build Tool
- **Gradle 8.x부터 동작**
- **Gradle 8.14.3 최적**
- **Gradle 8.5+ 권장**

---

## 아키텍처

본 프로젝트는 **Hexagonal Architecture (Port & Adapter Pattern)**를 채택하고 있습니다.

```
┌─────────────────────────────────────────────────┐
│              Adapter Layer                      │
│  ┌──────────────┐  ┌──────────────────────┐     │
│  │     Web      │  │    Persistence       │     │
│  │  (Controller)│  │   (JpaRepository)    │     │
│  └──────┬───────┘  └──────────┬───────────┘     │
└─────────┼─────────────────────┼─────────────────┘
          │                     │
┌─────────▼─────────────────────▼─────────────────┐
│           Application Layer                     │
│  ┌──────────────┐  ┌──────────────────────┐     │
│  │   Port-In    │  │      Service         │     │
│  │  (UseCase)   │  │  (Business Logic)    │     │
│  └──────────────┘  └──────────────────────┘     │
└─────────────────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────┐
│              Domain Layer                       │
│         (Entity, Value Object, Enum)            │
└─────────────────────────────────────────────────┘
```

### 계층별 역할

#### 1. Domain Layer (`domain`)
- **순수 비즈니스 로직**과 **도메인 모델**
- 외부 의존성 없음 (프레임워크 독립)
- Entity, Value Object, Enum, Domain Service

**예시:**
```java
// record 기반 불변 객체
public record Task(
    Long id,
    String name,
    TaskColor color,
    Long studyTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    User user
) {
    public static Task create(String name, TaskColor color, User user) {
        return new Task(null, name, color, 0L,
                       LocalDateTime.now(), LocalDateTime.now(), user);
    }
}
```

#### 2. Application Layer (`application`)
- 비즈니스 유스케이스 구현
- Port (인터페이스) 정의
- Service, Data, Port 패키지 포함

**구조:**
```
application/
├── {feature}/
│   ├── service/         # 비즈니스 로직 구현
│   │   └── GetChapterRankingService.java
│   ├── port/
│   │   └── in/          # UseCase 인터페이스
│   │       └── GetChapterRankingUseCase.java
│   └── data/            # Command/Result DTO
│       └── GetChapterRankingData.java
```

#### 3. Adapter Layer (`adapter`)
- 외부 세계와의 통신 담당
- Web, Persistence, External API, ExceptionHandler 등

**구조:**
```
adapter/
├── web/                 # REST API
│   ├── {feature}/
│   │   ├── controller/
│   │   ├── dto/
│   │   └── docs/        # Swagger 문서
├── persistence/         # DB 접근
│   ├── {entity}/
│   │   ├── {Entity}JpaEntity.java
│   │   ├── {Entity}JpaRepository.java
│   │   └── {Entity}JpaAdapter.java
└── github/             # 외부 API 연동
```

#### 4. Infrastructure Layer (`infrastructure`)
- 기술적 설정 및 공통 인프라
- Config, Security 등

---

## 패키지 구조

```
com.process.clash/
│
├── domain/                          # 도메인 계층
│   ├── record/
│   │   ├── model/
│   │   │   ├── entity/             # 도메인 엔티티
│   │   │   └── enums/              # 도메인 Enum
│   ├── roadmap/
│   ├── user/
│   └── common/
│       └── enums/                   # 공통 Enum
│
├── application/                     # 애플리케이션 계층
│   ├── ranking/
│   │   ├── service/                # 비즈니스 로직
│   │   │   └── GetChapterRankingService.java
│   │   ├── port/
│   │   │   └── in/                 # UseCase 인터페이스
│   │   │       └── GetChapterRankingUseCase.java
│   │   └── data/                   # Command/Result DTO
│   │       └── GetChapterRankingData.java
│   ├── record/
│   ├── compete/
│   └── common/
│       └── actor/                  # 인증된 사용자 정보
│
├── adapter/                         # 어댑터 계층
│   ├── web/                        # REST API 어댑터
│   │   ├── ranking/
│   │   │   ├── controller/        # 컨트롤러
│   │   │   │   └── RankingController.java
│   │   │   ├── dto/               # Request/Response DTO
│   │   │   │   └── GetChapterRankingDto.java
│   │   │   └── docs/              # API 문서화
│   │   │       ├── controller/    # Controller 문서 인터페이스
│   │   │       └── response/      # Response 예시 문서
│   │   ├── common/
│   │   │   ├── ApiResponse.java
│   │   │   └── GlobalExceptionHandler.java
│   │   └── security/
│   │       └── AuthenticatedActor.java
│   ├── persistence/                # 영속성 어댑터
│   │   ├── roadmap/
│   │   │   ├── sectionprogress/
│   │   │   │   ├── UserSectionProgressJpaEntity.java
│   │   │   │   ├── UserSectionProgressJpaRepository.java
│   │   │   │   └── UserSectionProgressJpaAdapter.java
│   │   └── user/
│   ├── github/                     # 외부 API 어댑터
│   └── scheduler/                  # 스케줄러
│
└── infrastructure/                  # 인프라 계층
    ├── config/                     # 설정
    │   ├── SecurityConfig.java
    │   ├── OpenApiConfig.java
    │   └── RedisConfig.java
    ├── security/
    └── web/
```

---

## 코딩 컨벤션

### 1. 네이밍 규칙

#### 클래스명
- **Service**: `{동사}{명사}Service`
  - 예: `GetChapterRankingService`, `CreateTaskService`
- **UseCase**: `{동사}{명사}UseCase`
  - 예: `GetChapterRankingUseCase`
- **Controller**: `{도메인}Controller`
  - 예: `RankingController`, `RecordController`
- **DTO**: `{동사}{명사}Dto`
  - 예: `GetChapterRankingDto`
- **Data**: `{동사}{명사}Data`
  - 예: `GetChapterRankingData`
- **Entity (JPA)**: `{명사}JpaEntity`
  - 예: `UserSectionProgressJpaEntity`
- **Repository**: `{명사}JpaRepository`
  - 예: `UserSectionProgressJpaRepository`
- **Domain Entity**: `{명사}` (접미사 없음)
  - 예: `Task`, `User`
- **Swagger docs 클래스** (`adapter/web/**/docs/**`): `{기능명}Document`
  - 예: `CreateTaskRequestDocument`, `GetRankingResponseDocument`, `RankingControllerDocument`

#### 메서드명
- **UseCase 메서드**: `execute(Command command)`
- **Service 메서드**: 동사로 시작
  - 예: `create()`, `update()`, `delete()`, `find()`
- **Repository 쿼리**: Spring Data JPA 규칙 준수
  - 예: `findByUserIdAndSectionId()`, `findAllByUserId()`

#### 변수명
- **camelCase** 사용
- 의미 있는 이름 사용
  - 좋음: `completedChaptersCount`, `userRank`
  - 나쁨: `cnt`, `tmp`, `data`

### 2. Java Record 활용
Domain 엔티티와 DTO는 **record**를 적극 활용합니다.

```java
// Domain Entity
public record Task(
    Long id,
    String name,
    TaskColor color,
    Long studyTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    User user
) {
    public static Task create(String name, TaskColor color, User user) {
        return new Task(null, name, color, 0L,
                       LocalDateTime.now(), LocalDateTime.now(), user);
    }
}

// Data Transfer Object
public class GetChapterRankingData {
    public record Command(Actor actor) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
        MyRankingVo myRank,
        List<RankersVo> allRankers
    ) {}
}
```

### 3. Lombok 사용
JPA Entity에는 **Lombok**을 사용합니다.

```java
@Entity
@Table(name = "user_section_progress")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSectionProgressJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... fields
}
```

**참고 애너테이션:**
- `@RequiredArgsConstructor` (의존성 주입 시 사용)
- `@Builder` (사용 지양)

### 4. 주석 규칙

#### 필요한 경우
- 복잡한 비즈니스 로직 설명
- 특이한 구현 이유 설명
- 외부 의존성 관련 주의사항

```java
// 현재 사용자의 랭킹 정보 저장
if (userId.equals(command.actor().id())) {
    myRank = new GetChapterRankingDto.MyRankingVo(...);
}
```

#### 불필요한 경우
- 자명한 코드
- 메서드명/변수명으로 충분히 설명되는 경우

### 5. 예외 처리
추상 클래스를 이용한 상속을 통해 최종적으로 `GlobalExceptionHandler`에서 전역 예외를 처리합니다.

```java
// Service에서는 비즈니스 예외를 던짐
if (user == null) {
    throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
}
```

---

## 커밋 메시지 컨벤션

### 기본 형식
```
{type}: {subject}
```

### Type 종류
- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `delete`: 코드 삭제
- `refactor`: 리팩토링
- `test`: 테스트 코드 추가/수정
- `docs`: 문서 수정
- `style`: 코드 포맷팅
- `chore`: 빌드 설정, 패키지 매니저 등
- `merge`: 브랜치 병합
- `hotfix`: 긴급 수정
- `comment`: 주석 추가/수정

### 예시
```bash
feat: section ranking 구현
merge: feat/compete/#114
hotfix: samesite: None으로 설정
```

### 주의사항
- **한글 사용**
- **간결하게 작성** (50자 이내 권장)
- **이슈 번호 포함** (필요시)

---

## 주요 패턴

### 1. UseCase 패턴
모든 비즈니스 로직은 **UseCase 인터페이스**를 통해 노출됩니다.

```java
// Port-In (UseCase)
public interface GetChapterRankingUseCase {
    GetChapterRankingData.Result execute(GetChapterRankingData.Command command);
}

// Service가 UseCase를 구현
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetChapterRankingService implements GetChapterRankingUseCase {
    @Override
    public GetChapterRankingData.Result execute(GetChapterRankingData.Command command) {
        // 비즈니스 로직
    }
}
```

### 2. Command-Result 패턴
모든 UseCase는 **Command 입력**을 받아 **Result 출력**을 반환합니다.

```java
public class GetChapterRankingData {
    // 입력
    public record Command(Actor actor) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    // 출력
    public record Result(
        MyRankingVo myRank,
        List<RankersVo> allRankers
    ) {}
}
```

### 3. ApiResponse 패턴
모든 API는 표준화된 **ApiResponse**를 반환합니다.

```java
ApiResponse.success(data, "성공 메시지");
ApiResponse.error("에러 메시지");
```

**응답 구조:**
```json
{
  "success": true,
  "message": "챕터 완료 수 랭킹 조회를 성공했습니다.",
  "data": {
    "myRank": { ... },
    "allRankers": [ ... ]
  }
}
```

### 4. Native Query 활용
복잡한 쿼리는 **Native Query + 커스텀 record 반환**을 사용합니다.
+만약 서비스 Layer애서 추가적인 타입 변환이 바로 필요하거나 확장성이 떨어진다고 판단된다면
 **Native Query + Object[] 반환**을 사용합니다.

```java
@Query(value = """
    SELECT
        user_id AS userId,
        cast(date_trunc('week', study_date) as date) AS recordedDate,
        AVG(commit_count + pr_count + review_count + issue_count) AS point
    FROM github_daily_stats
    WHERE user_id IN (:userIds)
        AND study_date >= date_trunc('week', CAST(:startDate AS date))
        AND study_date < :endDate
    GROUP BY user_id, date_trunc('week', study_date)
    ORDER BY user_id, date_trunc('week', study_date) ASC
""", nativeQuery = true)
List<Object[]> findWeeklyContributionsByUserIds(
    @Param("userIds") List<Long> userIds,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate,
    Pageable pageable
);
```

**장점:**
- N+1 문제 방지
- 성능 최적화
- 복잡한 집계 쿼리 가능

### 5. Swagger 문서화 패턴
Controller는 **Document 인터페이스를 구현**하여 Swagger 문서를 분리합니다.

```java
// Document 인터페이스
@Tag(name = "랭킹 API")
public interface RankingControllerDocument {
    @Operation(summary = "챕터 랭킹 조회")
    ApiResponse<GetChapterRankingDto.Response> getChapterRanking(Actor actor);
}

// Controller 구현
@RestController
@RequestMapping("/api/rankings")
public class RankingController implements RankingControllerDocument {
    // 실제 구현
}
```

---

## 성능 최적화 가이드

### N+1 문제 방지
1. **Native Query 활용**: 복잡한 조회는 Native Query로 한 번에 처리
2. **Object[] 반환**: JPA Entity 대신 필요한 데이터만 Object[]로 반환
3. **Fetch Join**: 필요시 `@EntityGraph` 또는 Fetch Join 사용

### 쿼리 최적화 체크리스트
- [ ] N+1 문제가 없는가?
- [ ] 필요한 데이터만 조회하는가?
- [ ] 인덱스가 적절히 설정되었는가?
- [ ] 불필요한 JOIN이 없는가?

---

## 브랜치 전략

```
main (프로덕션)
  ↑
develop (개발)
  ↑
feat/{feature-name}/#{issue-number} (기능 개발)
```

**예시:**
- `feat/section-ranking/#147`
- `fix/n-plus-one/#152`

---

## 개발 환경 설정

### 필수 설치
1. Java 21
2. PostgreSQL
3. Redis
4. Gradle 8.x

### 프로젝트 실행
```bash
# 의존성 다운로드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test
```

### API 문서 접속
```
http://localhost:8080/swagger-ui.html
```

---

## 추가 참고 자료

### 도메인별 핵심 기능
- **Ranking**: 챕터/섹션 완료 수 기준 랭킹
- **Record**: 학습 시간 기록
- **Compete**: 라이벌 경쟁, 배틀
- **Roadmap**: 카테고리, 섹션, 챕터, 미션
- **Shop**: 상품, 시즌, 추천 상품
- **Auth**: 회원가입, 로그인, 이메일 인증

### 문의 사항
프로젝트 관련 문의는 이슈를 등록해주세요.

---

**마지막 업데이트**: 2026-02-06
