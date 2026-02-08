-- Major Question
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (1, '웹사이트의 레이아웃을 잡고 시각적으로 예쁘게 꾸미는 것이 즐거운가요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 1, 0, 0, 0, 5);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (2, '서버 성능을 최적화하고 대규모 트래픽을 견디는 설계를 하는 데 흥미가 있나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 0, 0, 0, 5, 1);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (3, '내 손안의 스마트폰에서 돌아가는 앱을 직접 개발하고 배포하고 싶나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 0, 5, 1, 0, 0);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (4, '화려한 3D 그래픽이나 게임 캐릭터의 움직임을 제어하는 로직을 만들고 싶나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 0, 1, 5, 0, 0);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (5, '데이터를 학습시켜 인공지능이 스스로 판단하게 만드는 과정이 신기하고 재미있나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 5, 0, 0, 1, 0);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (6, '브라우저에서 실행되는 복잡한 웹 애플리케이션의 기능을 구현하는 것을 좋아하나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 1, 0, 0, 3, 5);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (7, '다양한 기기 환경(모바일, 태블릿)에 맞춰 최적화된 화면을 만드는 것에 관심이 있나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 0, 5, 3, 0, 1);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (8, '수학적 알고리즘을 활용해 복잡한 문제를 효율적으로 해결하는 것을 선호하시나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 5, 1, 0, 4, 0);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (9, '사용자가 몰입할 수 있는 가상 세계나 인터랙티브한 콘텐츠 제작을 선호하시나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 0, 2, 5, 0, 2);
INSERT INTO public.major_questions (id, content, created_at, updated_at, ai, app, game, server, web) VALUES (10, '보이지 않는 곳에서 데이터를 안전하게 저장하고 관리하는 시스템 구축에 관심이 있나요?', '2026-01-21 04:47:07.828724', '2026-01-21 04:47:07.828724', 1, 4, 0, 5, 0);

-- Category
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (1, '2026-01-21 02:46:05.390065', 'Java', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (2, '2026-01-21 02:46:05.390065', 'Spring Boot', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (3, '2026-01-21 02:46:05.390065', 'HTML', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (4, '2026-01-21 02:46:05.390065', 'CSS', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (5, '2026-01-21 02:46:05.390065', 'JavaScript', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (6, '2026-01-21 02:46:05.390065', 'React', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (7, '2026-01-21 02:46:05.390065', 'TypeScript', '2026-01-21 02:46:05.390065');
INSERT INTO public.categories (id, created_at, name, updated_at) VALUES (8, '2026-01-21 02:46:05.390065', 'Next.js', '2026-01-21 02:46:05.390065');

-- Section
-- 서버 전공 섹션
-- 서버 전공 섹션
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (1, '2026-01-21 02:46:05.394152', '입출력, 변수, 연산자, 조건문, 반복문, 배열, 함수부터 클래스, 상속, 캡슐화까지 Java 기초를 학습합니다.', 'SERVER', 0, 'Java 기초', '2026-01-21 02:46:05.394152', 1);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (2, '2026-01-21 02:46:05.394152', '인터페이스, 추상화, 다형성, 예외 처리, 제네릭, 컬렉션, 람다/스트림, SOLID 원칙을 학습합니다.', 'SERVER', 1, 'Java 중급', '2026-01-21 02:46:05.394152', 1);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (3, '2026-01-21 02:46:05.394152', '멀티스레딩 기본 개념, 동기화, 고급 활용법을 통해 동시성 프로그래밍을 학습합니다', 'SERVER', 2, 'Java 심화', '2026-01-21 02:46:05.394152', 1);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (4, '2026-01-21 02:46:05.394152', 'Controller, Service, Repository, DTO, Domain 계층 구조와 JPA, CRUD API, Security, 테스트 코드를 학습합니다.', 'SERVER', 3, 'Spring Boot 기본', '2026-01-21 02:46:05.394152', 2);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (5, '2026-01-21 02:46:05.394152', 'QueryDSL, JWT 인증, Security 심화, WebSocket, Clean/Hexagonal/Layered Architecture, AOP, 예외 처리 전략을 학습합니다.', 'SERVER', 4, 'Spring Boot 실무', '2026-01-21 02:46:05.394152', 2);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (6, '2026-01-21 02:46:05.394152', 'N+1 문제, Fetch 조인, 복합키, 순환 참조 해결과 배포와 같은 실무에서 자주 일어나는 문제를 학습합니다.', 'SERVER', 5, 'Spring Boot 문제 해결', '2026-01-21 02:46:05.394152', 2);

-- 웹 전공 섹션
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (7, '2026-01-21 07:30:08.914441', '웹의 뼈대를 잡는 시맨틱 태그와 기본 문법을 마스터합니다.', 'WEB', 0, 'HTML', '2026-01-21 07:30:08.914441', 3);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (8, '2026-01-21 07:31:01.451657', '웹 요소의 레이아웃, 스타일링, 반응형 디자인을 마스터합니다.', 'WEB', 1, 'CSS', '2026-01-21 07:31:01.451657', 4);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (9, '2026-01-21 07:32:36.041266', '변수, 함수, DOM 조작부터 비동기 처리까지 JavaScript를 다룹니다.', 'WEB', 2, 'JavaScript', '2026-01-21 07:32:36.041266', 5);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (10, '2026-01-21 09:00:02.899529', '컴포넌트 기반 개발과 Hooks를 활용한 상태 관리를 마스터합니다.', 'WEB', 3, 'React', '2026-01-21 09:00:02.899529', 6);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (11, '2026-01-21 09:00:34.0781', '정적 타이핑과 고급 타입 시스템을 활용한 안전한 개발을 학습합니다.', 'WEB', 4, 'TypeScript', '2026-01-21 09:00:34.0781', 7);
INSERT INTO public.sections (id, created_at, description, major, order_index, title, updated_at, category_id) VALUES (12, '2026-01-21 09:05:25.357019', 'App Router, 렌더링 전략, 서버 액션을 활용한 풀스택 개발을 배웁니다.', 'WEB', 5, 'Next.js', '2026-01-21 09:05:25.357019', 8);

-- Chapter
-- Java 기초 (section_id = 1)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (1, '2026-01-21 02:46:05.398038', 'Scanner를 이용한 입력과 System.out을 통한 출력을 학습합니다.', 0, '입출력', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (2, '2026-01-21 02:46:05.398038', '기본형과 참조형, 변수 선언과 초기화를 배웁니다.', 1, '변수와 자료형', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (3, '2026-01-21 02:46:05.398038', '산술, 비교, 논리 연산자의 우선순위와 활용법을 익힙니다.', 2, '연산자', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (4, '2026-01-21 02:46:05.398038', 'if, else, switch 문으로 프로그램의 흐름을 제어합니다.', 3, '조건문', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (5, '2026-01-21 02:46:05.398038', 'for, while, do-while 문의 차이와 활용법을 학습합니다.', 4, '반복문', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (6, '2026-01-21 02:46:05.398038', '1차원, 2차원 배열의 선언과 순회 방법을 배웁니다.', 5, '배열', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (7, '2026-01-21 02:46:05.398038', '메서드 정의, 매개변수, 반환값의 개념을 익힙니다.', 6, '함수', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (8, '2026-01-21 02:46:05.398038', '같은 이름의 메서드를 여러 형태로 정의하는 방법을 배웁니다.', 7, '오버로딩', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (9, '2026-01-21 02:46:05.398038', '지역 변수, 인스턴스 변수, 클래스 변수의 스코프를 이해합니다.', 8, '변수 생명 주기', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (10, '2026-01-21 02:46:05.398038', '객체지향의 시작, 클래스 설계와 객체 생성을 학습합니다.', 9, '클래스', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (11, '2026-01-21 02:46:05.398038', '부모-자식 관계와 코드 재사용의 핵심 개념을 배웁니다.', 10, '상속', '2026-01-21 02:46:05.398038', 1);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (12, '2026-01-21 02:46:05.398038', '접근 제어자로 데이터를 보호하는 방법을 익힙니다.', 11, '캡슐화', '2026-01-21 02:46:05.398038', 1);
-- Java 중급 (section_id = 2)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (13, '2026-01-21 02:46:05.398038', '계약 기반 프로그래밍과 다중 구현을 배웁니다.', 0, '인터페이스', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (14, '2026-01-21 02:46:05.398038', '추상 클래스와 추상 메서드의 활용법을 학습합니다.', 1, '추상화', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (15, '2026-01-21 02:46:05.398038', '오버라이딩과 동적 바인딩의 원리를 이해합니다.', 2, '다형성', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (16, '2026-01-21 02:46:05.398038', 'try-catch-finally와 사용자 정의 예외를 다룹니다.', 3, '예외 처리', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (17, '2026-01-21 02:46:05.398038', '타입 안전성을 보장하는 제네릭 클래스와 메서드를 배웁니다.', 4, '제네릭', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (18, '2026-01-21 02:46:05.398038', 'List, Set, Map 등 자료구조의 특징과 사용법을 익힙니다.', 5, '컬렉션 프레임워크', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (19, '2026-01-21 02:46:05.398038', '람다식과 스트림 API로 함수형 프로그래밍을 학습합니다.', 6, '변형 함수 (람다/스트림 등)', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (20, '2026-01-21 02:46:05.398038', '상수 집합을 안전하게 관리하는 Enum의 활용법을 배웁니다.', 7, '열거형', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (21, '2026-01-21 02:46:05.398038', '클래스는 하나의 책임만 가져야 한다는 원칙을 이해합니다.', 8, 'SOLID - S (단일 책임)', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (22, '2026-01-21 02:46:05.398038', '확장에는 열려있고 수정에는 닫혀있어야 하는 원칙을 배웁니다.', 9, 'SOLID - O (개방-폐쇄)', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (23, '2026-01-21 02:46:05.398038', '자식 클래스는 부모 클래스를 대체할 수 있어야 한다는 원칙을 학습합니다.', 10, 'SOLID - L (리스코프 치환)', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (24, '2026-01-21 02:46:05.398038', '클라이언트는 사용하지 않는 인터페이스에 의존하지 않아야 한다는 원칙을 익힙니다.', 11, 'SOLID - I (인터페이스 분리)', '2026-01-21 02:46:05.398038', 2);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (25, '2026-01-21 02:46:05.398038', '고수준 모듈은 저수준 모듈에 의존하지 않아야 한다는 원칙을 배웁니다.', 12, 'SOLID - D (의존성 역전)', '2026-01-21 02:46:05.398038', 2);
-- Java 심화 (section_id = 3)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (26, '2026-01-21 02:46:05.398038', 'Thread 클래스와 Runnable 인터페이스의 기본 개념을 배웁니다.', 0, '스레드 - 기본 개념', '2026-01-21 02:46:05.398038', 3);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (27, '2026-01-21 02:46:05.398038', 'synchronized와 Lock을 이용한 동시성 제어를 학습합니다.', 1, '스레드 - 동기화', '2026-01-21 02:46:05.398038', 3);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (28, '2026-01-21 02:46:05.398038', 'ExecutorService와 CompletableFuture의 활용법을 익힙니다.', 2, '스레드 - 고급 활용', '2026-01-21 02:46:05.398038', 3);
-- Spring Boot 기본 (section_id = 4)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (29, '2026-01-21 02:46:05.398038', 'Spring Boot 프로젝트 생성과 기본 설정을 배웁니다.', 0, 'Spring Boot 시작하기', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (30, '2026-01-21 02:46:05.398038', 'HTTP 요청을 처리하는 컨트롤러 계층을 학습합니다.', 1, 'Controller', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (31, '2026-01-21 02:46:05.398038', '비즈니스 로직을 담당하는 서비스 계층을 익힙니다.', 2, 'Service', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (32, '2026-01-21 02:46:05.398038', '데이터베이스 접근을 담당하는 리포지토리 계층을 배웁니다.', 3, 'Repository', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (33, '2026-01-21 02:46:05.398038', '계층 간 데이터 전달 객체의 역할과 설계를 학습합니다.', 4, 'DTO', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (34, '2026-01-21 02:46:05.398038', '비즈니스 도메인을 표현하는 엔티티 설계를 익힙니다.', 5, 'Domain', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (35, '2026-01-21 02:46:05.398038', 'JPA와 Hibernate를 이용한 ORM의 기초를 배웁니다.', 6, 'Database & JPA 기초', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (36, '2026-01-21 02:46:05.398038', 'RESTful API의 생성, 조회, 수정, 삭제를 구현합니다.', 7, 'CRUD API 구축', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (37, '2026-01-21 02:46:05.398038', 'Spring Security의 기본 구조와 인증/인가를 학습합니다.', 8, 'Security 기초', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (38, '2026-01-21 02:46:05.398038', '세션 기반 인증 시스템을 구현하는 방법을 배웁니다.', 9, 'Session 로그인', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (39, '2026-01-21 02:46:05.398038', 'JUnit과 Mockito를 활용한 단위 테스트를 익힙니다.', 10, '테스트 코드 작성', '2026-01-21 02:46:05.398038', 4);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (40, '2026-01-21 02:46:05.398038', '전역 예외 처리와 커스텀 예외 설계를 학습합니다.', 11, '예외 처리', '2026-01-21 02:46:05.398038', 4);
-- Spring Boot 실무 (section_id = 5)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (41, '2026-01-21 02:46:05.398038', 'DB 성능 향상을 위한 인덱스와 시퀀스 전략을 배웁니다.', 0, 'Index와 Sequence', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (42, '2026-01-21 02:46:05.398038', '타입 안전한 쿼리 작성을 위한 QueryDSL을 학습합니다.', 1, 'QueryDSL', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (43, '2026-01-21 02:46:05.398038', 'JWT 토큰 기반 인증 시스템을 구현합니다.', 2, 'JWT 인증 시스템', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (44, '2026-01-21 02:46:05.398038', 'Security 필터 체인과 권한 관리를 심화 학습합니다.', 3, 'Security 심화', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (45, '2026-01-21 02:46:05.398038', 'WebSocket과 STOMP를 이용한 실시간 통신을 배웁니다.', 4, 'WebSocket & 실시간 통신', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (46, '2026-01-21 02:46:05.398038', '도메인 중심의 클린 아키텍처 설계 원칙을 익힙니다.', 5, 'Clean Architecture', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (47, '2026-01-21 02:46:05.398038', '도메인별 모듈과 전역 공통 모듈의 분리 전략을 학습합니다.', 6, 'Domain-Global Architecture', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (48, '2026-01-21 02:46:05.398038', '전통적인 계층형 아키텍처의 구조와 장단점을 배웁니다.', 7, 'Layered Architecture', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (49, '2026-01-21 02:46:05.398038', '포트와 어댑터 패턴 기반의 육각형 아키텍처를 익힙니다.', 8, 'Hexagonal Architecture', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (50, '2026-01-21 02:46:05.398038', '관점 지향 프로그래밍과 프록시 패턴을 학습합니다.', 9, 'AOP & Proxy', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (51, '2026-01-21 02:46:05.398038', '체계적인 예외 처리 전략과 에러 응답 설계를 배웁니다.', 10, '예외 처리 전략', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (52, '2026-01-21 02:46:05.398038', 'Docker, GitHub Actions를 이용한 자동 배포 파이프라인을 구축합니다.', 11, '배포 & CI/CD', '2026-01-21 02:46:05.398038', 5);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (53, '2026-01-21 02:46:05.398038', '로그 수집, 성능 모니터링, 알림 시스템을 설정합니다.', 12, '운영 & 모니터링', '2026-01-21 02:46:05.398038', 5);
-- Spring Boot 문제 해결 (section_id = 6)
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (54, '2026-01-21 02:46:05.398038', '연관 엔티티를 한 번에 조회하는 Fetch Join을 배웁니다.', 0, 'Fetch 조인', '2026-01-21 02:46:05.398038', 6);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (55, '2026-01-21 02:46:05.398038', '불필요한 쿼리 발생을 방지하는 최적화 기법을 익힙니다.', 1, 'N+1 문제 해결', '2026-01-21 02:46:05.398038', 6);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (56, '2026-01-21 02:46:05.398038', '@IdClass와 @EmbeddedId를 이용한 복합키 매핑을 학습합니다.', 2, '복합키 속성', '2026-01-21 02:46:05.398038', 6);
INSERT INTO public.chapters (id, created_at, description, order_index, title, updated_at, section_id) VALUES (57, '2026-01-21 02:46:05.398038', '양방향 연관관계에서 발생하는 순환 참조를 해결합니다.', 3, '순환 참조 해결', '2026-01-21 02:46:05.398038', 6);

-- Choice
