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

-- Mission
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (4, '2026-01-21 02:46:29.445969', 1, 'SERVER 입문 과정', '2026-01-21 02:46:29.445969', 2, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (5, '2026-01-21 02:46:29.445969', 2, 'SERVER 중급 실습', '2026-01-21 02:46:29.445969', 2, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (6, '2026-01-21 02:46:29.445969', 3, 'SERVER 심화 문제', '2026-01-21 02:46:29.445969', 2, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (7, '2026-01-21 02:46:29.445969', 1, 'AI 입문 과정', '2026-01-21 02:46:29.445969', 3, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (8, '2026-01-21 02:46:29.445969', 2, 'AI 중급 실습', '2026-01-21 02:46:29.445969', 3, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (9, '2026-01-21 02:46:29.445969', 3, 'AI 심화 문제', '2026-01-21 02:46:29.445969', 3, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (13, '2026-01-21 07:26:40.991282', 1, 'IoC & DI Container (기초 다지기)', '2026-01-21 07:26:40.991282', 2, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (14, '2026-01-21 07:26:40.991282', 2, 'IoC & DI Container (실전 응용)', '2026-01-21 07:26:40.991282', 2, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (15, '2026-01-21 07:26:40.991282', 3, 'IoC & DI Container (심화 마스터)', '2026-01-21 07:26:40.991282', 2, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (16, '2026-01-21 07:26:40.991282', 1, 'Pandas Library (기초 다지기)', '2026-01-21 07:26:40.991282', 3, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (17, '2026-01-21 07:26:40.991282', 2, 'Pandas Library (실전 응용)', '2026-01-21 07:26:40.991282', 3, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (18, '2026-01-21 07:26:40.991282', 3, 'Pandas Library (심화 마스터)', '2026-01-21 07:26:40.991282', 3, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (22, '2026-01-21 07:26:40.991282', 1, 'Spring Core - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 5, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (23, '2026-01-21 07:26:40.991282', 2, 'Spring Core - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 5, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (24, '2026-01-21 07:26:40.991282', 3, 'Spring Core - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 5, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (25, '2026-01-21 07:26:40.991282', 1, 'Python Analysis - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 6, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (26, '2026-01-21 07:26:40.991282', 2, 'Python Analysis - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 6, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (27, '2026-01-21 07:26:40.991282', 3, 'Python Analysis - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 6, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (37, '2026-01-21 07:26:40.991282', 1, 'Java Programming - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 10, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (38, '2026-01-21 07:26:40.991282', 2, 'Java Programming - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 10, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (39, '2026-01-21 07:26:40.991282', 3, 'Java Programming - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 10, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (40, '2026-01-21 07:26:40.991282', 1, 'Spring Core Princicple - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 11, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (41, '2026-01-21 07:26:40.991282', 2, 'Spring Core Princicple - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 11, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (42, '2026-01-21 07:26:40.991282', 3, 'Spring Core Princicple - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 11, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (43, '2026-01-21 07:26:40.991282', 1, 'Database Design - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 12, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (44, '2026-01-21 07:26:40.991282', 2, 'Database Design - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 12, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (45, '2026-01-21 07:26:40.991282', 3, 'Database Design - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 12, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (46, '2026-01-21 07:26:40.991282', 1, 'Python for Data - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 13, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (47, '2026-01-21 07:26:40.991282', 2, 'Python for Data - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 13, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (48, '2026-01-21 07:26:40.991282', 3, 'Python for Data - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 13, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (49, '2026-01-21 07:26:40.991282', 1, 'Machine Learning - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 14, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (50, '2026-01-21 07:26:40.991282', 2, 'Machine Learning - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 14, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (51, '2026-01-21 07:26:40.991282', 3, 'Machine Learning - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 14, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (52, '2026-01-21 07:26:40.991282', 1, 'Deep Learning - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 15, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (53, '2026-01-21 07:26:40.991282', 2, 'Deep Learning - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 15, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (54, '2026-01-21 07:26:40.991282', 3, 'Deep Learning - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 15, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (55, '2026-01-21 07:26:40.991282', 1, 'Unity Interface - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 16, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (56, '2026-01-21 07:26:40.991282', 2, 'Unity Interface - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 16, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (57, '2026-01-21 07:26:40.991282', 3, 'Unity Interface - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 16, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (58, '2026-01-21 07:26:40.991282', 1, 'C# Game Scripting - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 17, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (59, '2026-01-21 07:26:40.991282', 2, 'C# Game Scripting - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 17, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (60, '2026-01-21 07:26:40.991282', 3, 'C# Game Scripting - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 17, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (61, '2026-01-21 07:26:40.991282', 1, '3D Graphics/Physics - 상세 학습 (기초 다지기)', '2026-01-21 07:26:40.991282', 18, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (62, '2026-01-21 07:26:40.991282', 2, '3D Graphics/Physics - 상세 학습 (실전 응용)', '2026-01-21 07:26:40.991282', 18, 1);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (63, '2026-01-21 07:26:40.991282', 3, '3D Graphics/Physics - 상세 학습 (심화 마스터)', '2026-01-21 07:26:40.991282', 18, 2);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (64, '2026-01-21 07:30:25.797552', 1, '기본 구조 설계', '2026-01-21 07:30:25.797552', 19, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (65, '2026-01-21 07:30:25.797552', 2, '사용자 경험을 높이는 폼 설계', '2026-01-21 07:30:25.797552', 20, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (66, '2026-01-21 07:30:25.797552', 3, '실전 WAI-ARIA 활용', '2026-01-21 07:30:25.797552', 21, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (67, '2026-01-21 07:31:17.615458', 1, '스타일 충돌 해결하기', '2026-01-21 07:31:17.615458', 22, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (68, '2026-01-21 07:31:17.615458', 2, 'Flexbox 마스터', '2026-01-21 07:31:17.615458', 23, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (69, '2026-01-21 07:31:17.615458', 3, '고성능 애니메이션 구현', '2026-01-21 07:31:17.615458', 24, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (70, '2026-01-21 07:32:43.215497', 1, '변수의 생명 주기', '2026-01-21 07:32:43.215497', 25, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (71, '2026-01-21 07:32:43.215497', 2, 'Promise와 Async 활용', '2026-01-21 07:32:43.215497', 26, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (72, '2026-01-21 07:32:43.215497', 3, 'JS 엔진의 내부 동작', '2026-01-21 07:32:43.215497', 27, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (73, '2026-01-21 09:00:09.234665', 1, 'React 핵심 원리 이해', '2026-01-21 09:00:09.234665', 28, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (74, '2026-01-21 09:00:09.234665', 2, '효율적인 상태 관리', '2026-01-21 09:00:09.234665', 29, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (75, '2026-01-21 09:00:09.234665', 3, '렌더링 성능 극대화', '2026-01-21 09:00:09.234665', 30, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (76, '2026-01-21 09:05:29.268099', 1, 'Next.js 프로젝트 구조 설계', '2026-01-21 09:05:29.268099', 33, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (77, '2026-01-21 09:05:29.268099', 2, '실시간 데이터와 성능의 균형', '2026-01-21 09:05:29.268099', 34, 0);
INSERT INTO public.missions (id, created_at, difficulty, title, updated_at, fk_chapter_id, order_index) VALUES (78, '2026-01-21 09:05:29.268099', 3, '현대적 웹 아키텍처', '2026-01-21 09:05:29.268099', 35, 0);

-- Mission Question
-- Mission 4: SERVER 입문 과정 (변수와 자료형 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (1, 4, '변수와 자료형의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (2, 4, '변수와 자료형의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (3, 4, '변수와 자료형의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (4, 4, '변수와 자료형의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 5: SERVER 중급 실습 (변수와 자료형 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (5, 5, '변수와 자료형의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (6, 5, '변수와 자료형의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (7, 5, '변수와 자료형의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (8, 5, '변수와 자료형의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 6: SERVER 심화 문제 (변수와 자료형 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (9, 6, '변수와 자료형의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (10, 6, '변수와 자료형의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (11, 6, '변수와 자료형의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (12, 6, '변수와 자료형의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 7: AI 입문 과정 (연산자 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (13, 7, '연산자의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (14, 7, '연산자의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (15, 7, '연산자의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (16, 7, '연산자의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 8: AI 중급 실습 (연산자 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (17, 8, '연산자의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (18, 8, '연산자의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (19, 8, '연산자의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (20, 8, '연산자의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 9: AI 심화 문제 (연산자 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (21, 9, '연산자의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (22, 9, '연산자의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (23, 9, '연산자의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (24, 9, '연산자의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 13: IoC & DI Container (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (25, 13, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (26, 13, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (27, 13, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (28, 13, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 14: IoC & DI Container (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (29, 14, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (30, 14, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (31, 14, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (32, 14, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 15: IoC & DI Container (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (33, 15, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (34, 15, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (35, 15, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (36, 15, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 16: Pandas Library (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (37, 16, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (38, 16, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (39, 16, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (40, 16, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 17: Pandas Library (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (41, 17, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (42, 17, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (43, 17, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (44, 17, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 18: Pandas Library (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (45, 18, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (46, 18, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (47, 18, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (48, 18, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 22: Spring Core (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (49, 22, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (50, 22, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (51, 22, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (52, 22, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 23: Spring Core (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (53, 23, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (54, 23, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (55, 23, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (56, 23, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 24: Spring Core (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (57, 24, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (58, 24, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (59, 24, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (60, 24, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 25: Python Analysis (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (61, 25, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (62, 25, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (63, 25, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (64, 25, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 26: Python Analysis (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (65, 26, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (66, 26, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (67, 26, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (68, 26, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 27: Python Analysis (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (69, 27, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (70, 27, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (71, 27, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (72, 27, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 37: Java Programming (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (73, 37, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (74, 37, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (75, 37, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (76, 37, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 38: Java Programming (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (77, 38, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (78, 38, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (79, 38, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (80, 38, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 39: Java Programming (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (81, 39, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (82, 39, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (83, 39, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (84, 39, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 40: Spring Core Principle (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (85, 40, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (86, 40, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (87, 40, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (88, 40, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 41: Spring Core Principle (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (89, 41, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (90, 41, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (91, 41, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (92, 41, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 42: Spring Core Principle (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (93, 42, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (94, 42, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (95, 42, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (96, 42, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 43: Database Design (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (97, 43, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (98, 43, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (99, 43, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (100, 43, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 44: Database Design (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (101, 44, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (102, 44, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (103, 44, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (104, 44, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 45: Database Design (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (105, 45, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (106, 45, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (107, 45, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (108, 45, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 46: Python for Data (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (109, 46, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (110, 46, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (111, 46, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (112, 46, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 47: Python for Data (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (113, 47, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (114, 47, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (115, 47, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (116, 47, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 48: Python for Data (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (117, 48, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (118, 48, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (119, 48, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (120, 48, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 49: Machine Learning (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (121, 49, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (122, 49, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (123, 49, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (124, 49, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 50: Machine Learning (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (125, 50, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (126, 50, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (127, 50, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (128, 50, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 51: Machine Learning (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (129, 51, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (130, 51, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (131, 51, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (132, 51, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 52: Deep Learning (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (133, 52, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (134, 52, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (135, 52, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (136, 52, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 53: Deep Learning (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (137, 53, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (138, 53, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (139, 53, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (140, 53, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 54: Deep Learning (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (141, 54, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (142, 54, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (143, 54, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (144, 54, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 55: Unity Interface (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (145, 55, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (146, 55, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (147, 55, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (148, 55, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 56: Unity Interface (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (149, 56, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (150, 56, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (151, 56, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (152, 56, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 57: Unity Interface (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (153, 57, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (154, 57, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (155, 57, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (156, 57, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 58: C# Game Scripting (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (157, 58, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (158, 58, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (159, 58, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (160, 58, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 59: C# Game Scripting (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (161, 59, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (162, 59, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (163, 59, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (164, 59, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 60: C# Game Scripting (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (165, 60, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (166, 60, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (167, 60, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (168, 60, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 61: 3D Graphics/Physics (기초 다지기 - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (169, 61, '기초 다지기의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (170, 61, '기초 다지기의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (171, 61, '기초 다지기의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (172, 61, '기초 다지기의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 62: 3D Graphics/Physics (실전 응용 - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (173, 62, '실전 응용의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (174, 62, '실전 응용의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (175, 62, '실전 응용의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (176, 62, '실전 응용의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 63: 3D Graphics/Physics (심화 마스터 - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (177, 63, '심화 마스터의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (178, 63, '심화 마스터의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (179, 63, '심화 마스터의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (180, 63, '심화 마스터의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 64: 기본 구조 설계 (HTML - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (181, 64, 'HTML의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (182, 64, 'HTML의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (183, 64, 'HTML의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (184, 64, 'HTML의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 65: 사용자 경험을 높이는 폼 설계 (HTML - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (185, 65, 'HTML의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (186, 65, 'HTML의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (187, 65, 'HTML의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (188, 65, 'HTML의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 66: 실전 WAI-ARIA 활용 (HTML - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (189, 66, 'HTML의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (190, 66, 'HTML의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (191, 66, 'HTML의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (192, 66, 'HTML의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 67: 스타일 충돌 해결하기 (CSS - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (193, 67, 'CSS의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (194, 67, 'CSS의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (195, 67, 'CSS의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (196, 67, 'CSS의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 68: Flexbox 마스터 (CSS - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (197, 68, 'CSS의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (198, 68, 'CSS의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (199, 68, 'CSS의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (200, 68, 'CSS의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 69: 고성능 애니메이션 구현 (CSS - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (201, 69, 'CSS의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (202, 69, 'CSS의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (203, 69, 'CSS의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (204, 69, 'CSS의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 70: 변수의 생명 주기 (JavaScript - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (205, 70, 'JavaScript의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (206, 70, 'JavaScript의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (207, 70, 'JavaScript의 기본 사용법을 설명하시오.', '간단한 설명', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (208, 70, 'JavaScript의 기본 이용법을 설명하시오.', '간단한 설명', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 71: Promise와 Async 활용 (JavaScript - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (209, 71, 'JavaScript의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (210, 71, 'JavaScript의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (211, 71, 'JavaScript의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (212, 71, 'JavaScript의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 72: JS 엔진의 내부 동작 (JavaScript - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (213, 72, 'JavaScript의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (214, 72, 'JavaScript의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (215, 72, 'JavaScript의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (216, 72, 'JavaScript의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 73: React 핵심 원리 이해 (React - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (217, 73, 'React의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (218, 73, 'React의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (219, 73, 'React의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (220, 73, 'React의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 74: 효율적인 상태 관리 (React - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (221, 74, 'React의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (222, 74, 'React의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (223, 74, 'React의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (224, 74, 'React의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 75: 렌더링 성능 극대화 (React - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (225, 75, 'React의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (226, 75, 'React의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (227, 75, 'React의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (228, 75, 'React의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 76: Next.js 프로젝트 구조 설계 (Next.js - 난이도 1)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (229, 76, 'Next.js의 기본 개념을 설명하시오.', '핵심 개념에 대한 간단한 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (230, 76, 'Next.js의 주요 특징을 나열하시오.', '3-5가지 주요 특징', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (231, 76, 'Next.js의 기본 사용법을 작성하시오.', '기본 문법이나 사용 예시', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (232, 76, 'Next.js의 차이점을 설명하시오.', '유사 개념 간의 차이점', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 77: 실시간 데이터와 성능의 균형 (Next.js - 난이도 2)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (233, 77, 'Next.js의 동작 원리를 설명하시오.', '내부 동작 메커니즘', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (234, 77, 'Next.js의 활용 방법과 시나리오를 설명하시오.', '실전 활용 사례', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (235, 77, 'Next.js의 장단점을 비교하시오.', '각 방식의 장단점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (236, 77, 'Next.js의 최적화 기법을 설명하시오.', '성능 향상 방법', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission 78: 현대적 웹 아키텍처 (Next.js - 난이도 3)
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (237, 78, 'Next.js의 고급 개념과 원리를 설명하시오.', '심화 개념 설명', 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (238, 78, 'Next.js의 성능 최적화 전략을 설명하시오.', '고급 최적화 기법', 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (239, 78, 'Next.js의 실무 적용 시 주의사항을 설명하시오.', '실전 주의점', 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.mission_questions (id, fk_mission_id, content, explanation, order_index, created_at, updated_at)
VALUES (240, 78, 'Next.js의 아키텍처 패턴을 설명하시오.', '설계 패턴 및 아키텍처', 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');

-- Choice
-- Mission Question 1의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (1, 1, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (2, 1, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (3, 1, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (4, 1, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 2의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (5, 2, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (6, 2, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (7, 2, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (8, 2, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 3의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (9, 3, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (10, 3, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (11, 3, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (12, 3, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 4의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (13, 4, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (14, 4, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (15, 4, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (16, 4, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 5의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (17, 5, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (18, 5, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (19, 5, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (20, 5, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 6의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (21, 6, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (22, 6, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (23, 6, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (24, 6, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 7의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (25, 7, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (26, 7, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (27, 7, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (28, 7, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 8의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (29, 8, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (30, 8, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (31, 8, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (32, 8, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 9의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (33, 9, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (34, 9, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (35, 9, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (36, 9, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 10의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (37, 10, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (38, 10, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (39, 10, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (40, 10, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 11의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (41, 11, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (42, 11, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (43, 11, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (44, 11, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 12의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (45, 12, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (46, 12, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (47, 12, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (48, 12, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 13의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (49, 13, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (50, 13, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (51, 13, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (52, 13, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 14의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (53, 14, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (54, 14, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (55, 14, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (56, 14, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 15의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (57, 15, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (58, 15, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (59, 15, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (60, 15, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 16의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (61, 16, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (62, 16, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (63, 16, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (64, 16, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 17의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (65, 17, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (66, 17, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (67, 17, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (68, 17, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 18의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (69, 18, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (70, 18, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (71, 18, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (72, 18, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 19의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (73, 19, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (74, 19, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (75, 19, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (76, 19, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 20의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (77, 20, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (78, 20, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (79, 20, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (80, 20, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 21의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (81, 21, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (82, 21, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (83, 21, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (84, 21, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 22의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (85, 22, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (86, 22, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (87, 22, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (88, 22, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 23의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (89, 23, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (90, 23, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (91, 23, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (92, 23, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 24의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (93, 24, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (94, 24, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (95, 24, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (96, 24, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 25의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (97, 25, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (98, 25, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (99, 25, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (100, 25, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 26의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (101, 26, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (102, 26, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (103, 26, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (104, 26, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 27의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (105, 27, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (106, 27, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (107, 27, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (108, 27, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 28의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (109, 28, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (110, 28, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (111, 28, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (112, 28, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 29의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (113, 29, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (114, 29, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (115, 29, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (116, 29, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 30의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (117, 30, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (118, 30, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (119, 30, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (120, 30, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 31의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (121, 31, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (122, 31, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (123, 31, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (124, 31, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 32의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (125, 32, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (126, 32, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (127, 32, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (128, 32, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 33의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (129, 33, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (130, 33, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (131, 33, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (132, 33, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 34의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (133, 34, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (134, 34, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (135, 34, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (136, 34, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 35의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (137, 35, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (138, 35, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (139, 35, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (140, 35, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 36의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (141, 36, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (142, 36, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (143, 36, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (144, 36, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 37의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (145, 37, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (146, 37, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (147, 37, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (148, 37, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 38의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (149, 38, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (150, 38, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (151, 38, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (152, 38, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 39의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (153, 39, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (154, 39, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (155, 39, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (156, 39, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 40의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (157, 40, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (158, 40, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (159, 40, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (160, 40, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 41의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (161, 41, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (162, 41, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (163, 41, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (164, 41, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 42의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (165, 42, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (166, 42, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (167, 42, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (168, 42, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 43의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (169, 43, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (170, 43, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (171, 43, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (172, 43, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 44의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (173, 44, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (174, 44, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (175, 44, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (176, 44, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 45의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (177, 45, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (178, 45, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (179, 45, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (180, 45, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 46의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (181, 46, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (182, 46, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (183, 46, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (184, 46, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 47의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (185, 47, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (186, 47, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (187, 47, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (188, 47, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 48의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (189, 48, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (190, 48, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (191, 48, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (192, 48, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 49의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (193, 49, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (194, 49, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (195, 49, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (196, 49, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 50의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (197, 50, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (198, 50, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (199, 50, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (200, 50, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 51의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (201, 51, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (202, 51, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (203, 51, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (204, 51, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 52의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (205, 52, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (206, 52, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (207, 52, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (208, 52, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 53의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (209, 53, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (210, 53, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (211, 53, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (212, 53, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 54의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (213, 54, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (214, 54, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (215, 54, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (216, 54, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 55의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (217, 55, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (218, 55, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (219, 55, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (220, 55, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 56의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (221, 56, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (222, 56, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (223, 56, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (224, 56, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 57의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (225, 57, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (226, 57, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (227, 57, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (228, 57, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 58의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (229, 58, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (230, 58, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (231, 58, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (232, 58, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 59의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (233, 59, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (234, 59, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (235, 59, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (236, 59, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 60의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (237, 60, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (238, 60, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (239, 60, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (240, 60, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 61의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (241, 61, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (242, 61, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (243, 61, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (244, 61, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 62의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (245, 62, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (246, 62, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (247, 62, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (248, 62, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 63의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (249, 63, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (250, 63, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (251, 63, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (252, 63, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 64의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (253, 64, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (254, 64, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (255, 64, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (256, 64, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 65의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (257, 65, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (258, 65, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (259, 65, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (260, 65, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 66의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (261, 66, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (262, 66, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (263, 66, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (264, 66, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 67의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (265, 67, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (266, 67, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (267, 67, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (268, 67, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 68의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (269, 68, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (270, 68, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (271, 68, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (272, 68, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 69의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (273, 69, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (274, 69, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (275, 69, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (276, 69, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 70의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (277, 70, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (278, 70, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (279, 70, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (280, 70, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 71의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (281, 71, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (282, 71, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (283, 71, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (284, 71, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 72의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (285, 72, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (286, 72, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (287, 72, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (288, 72, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 73의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (289, 73, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (290, 73, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (291, 73, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (292, 73, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 74의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (293, 74, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (294, 74, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (295, 74, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (296, 74, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 75의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (297, 75, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (298, 75, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (299, 75, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (300, 75, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 76의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (301, 76, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (302, 76, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (303, 76, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (304, 76, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 77의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (305, 77, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (306, 77, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (307, 77, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (308, 77, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 78의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (309, 78, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (310, 78, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (311, 78, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (312, 78, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 79의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (313, 79, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (314, 79, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (315, 79, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (316, 79, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 80의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (317, 80, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (318, 80, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (319, 80, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (320, 80, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 81의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (321, 81, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (322, 81, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (323, 81, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (324, 81, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 82의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (325, 82, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (326, 82, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (327, 82, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (328, 82, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 83의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (329, 83, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (330, 83, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (331, 83, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (332, 83, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 84의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (333, 84, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (334, 84, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (335, 84, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (336, 84, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 85의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (337, 85, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (338, 85, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (339, 85, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (340, 85, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 86의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (341, 86, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (342, 86, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (343, 86, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (344, 86, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 87의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (345, 87, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (346, 87, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (347, 87, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (348, 87, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 88의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (349, 88, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (350, 88, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (351, 88, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (352, 88, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 89의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (353, 89, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (354, 89, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (355, 89, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (356, 89, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 90의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (357, 90, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (358, 90, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (359, 90, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (360, 90, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 91의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (361, 91, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (362, 91, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (363, 91, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (364, 91, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 92의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (365, 92, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (366, 92, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (367, 92, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (368, 92, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 93의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (369, 93, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (370, 93, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (371, 93, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (372, 93, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 94의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (373, 94, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (374, 94, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (375, 94, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (376, 94, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 95의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (377, 95, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (378, 95, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (379, 95, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (380, 95, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 96의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (381, 96, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (382, 96, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (383, 96, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (384, 96, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 97의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (385, 97, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (386, 97, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (387, 97, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (388, 97, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 98의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (389, 98, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (390, 98, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (391, 98, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (392, 98, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 99의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (393, 99, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (394, 99, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (395, 99, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (396, 99, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 100의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (397, 100, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (398, 100, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (399, 100, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (400, 100, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 101의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (401, 101, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (402, 101, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (403, 101, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (404, 101, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 102의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (405, 102, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (406, 102, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (407, 102, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (408, 102, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 103의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (409, 103, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (410, 103, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (411, 103, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (412, 103, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 104의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (413, 104, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (414, 104, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (415, 104, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (416, 104, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 105의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (417, 105, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (418, 105, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (419, 105, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (420, 105, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 106의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (421, 106, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (422, 106, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (423, 106, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (424, 106, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 107의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (425, 107, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (426, 107, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (427, 107, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (428, 107, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 108의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (429, 108, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (430, 108, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (431, 108, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (432, 108, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 109의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (433, 109, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (434, 109, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (435, 109, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (436, 109, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 110의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (437, 110, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (438, 110, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (439, 110, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (440, 110, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 111의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (441, 111, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (442, 111, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (443, 111, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (444, 111, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 112의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (445, 112, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (446, 112, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (447, 112, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (448, 112, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 113의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (449, 113, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (450, 113, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (451, 113, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (452, 113, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 114의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (453, 114, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (454, 114, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (455, 114, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (456, 114, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 115의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (457, 115, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (458, 115, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (459, 115, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (460, 115, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 116의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (461, 116, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (462, 116, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (463, 116, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (464, 116, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 117의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (465, 117, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (466, 117, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (467, 117, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (468, 117, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 118의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (469, 118, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (470, 118, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (471, 118, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (472, 118, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 119의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (473, 119, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (474, 119, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (475, 119, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (476, 119, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 120의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (477, 120, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (478, 120, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (479, 120, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (480, 120, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 121의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (481, 121, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (482, 121, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (483, 121, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (484, 121, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 122의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (485, 122, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (486, 122, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (487, 122, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (488, 122, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 123의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (489, 123, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (490, 123, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (491, 123, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (492, 123, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 124의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (493, 124, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (494, 124, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (495, 124, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (496, 124, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 125의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (497, 125, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (498, 125, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (499, 125, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (500, 125, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 126의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (501, 126, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (502, 126, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (503, 126, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (504, 126, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 127의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (505, 127, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (506, 127, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (507, 127, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (508, 127, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 128의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (509, 128, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (510, 128, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (511, 128, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (512, 128, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 129의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (513, 129, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (514, 129, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (515, 129, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (516, 129, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 130의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (517, 130, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (518, 130, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (519, 130, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (520, 130, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 131의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (521, 131, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (522, 131, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (523, 131, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (524, 131, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 132의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (525, 132, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (526, 132, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (527, 132, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (528, 132, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 133의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (529, 133, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (530, 133, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (531, 133, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (532, 133, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 134의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (533, 134, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (534, 134, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (535, 134, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (536, 134, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 135의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (537, 135, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (538, 135, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (539, 135, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (540, 135, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 136의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (541, 136, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (542, 136, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (543, 136, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (544, 136, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 137의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (545, 137, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (546, 137, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (547, 137, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (548, 137, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 138의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (549, 138, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (550, 138, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (551, 138, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (552, 138, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 139의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (553, 139, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (554, 139, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (555, 139, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (556, 139, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 140의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (557, 140, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (558, 140, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (559, 140, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (560, 140, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 141의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (561, 141, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (562, 141, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (563, 141, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (564, 141, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 142의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (565, 142, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (566, 142, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (567, 142, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (568, 142, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 143의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (569, 143, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (570, 143, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (571, 143, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (572, 143, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 144의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (573, 144, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (574, 144, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (575, 144, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (576, 144, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 145의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (577, 145, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (578, 145, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (579, 145, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (580, 145, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 146의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (581, 146, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (582, 146, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (583, 146, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (584, 146, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 147의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (585, 147, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (586, 147, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (587, 147, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (588, 147, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 148의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (589, 148, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (590, 148, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (591, 148, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (592, 148, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 149의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (593, 149, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (594, 149, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (595, 149, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (596, 149, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 150의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (597, 150, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (598, 150, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (599, 150, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (600, 150, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 151의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (601, 151, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (602, 151, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (603, 151, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (604, 151, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 152의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (605, 152, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (606, 152, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (607, 152, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (608, 152, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 153의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (609, 153, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (610, 153, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (611, 153, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (612, 153, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 154의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (613, 154, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (614, 154, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (615, 154, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (616, 154, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 155의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (617, 155, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (618, 155, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (619, 155, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (620, 155, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 156의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (621, 156, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (622, 156, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (623, 156, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (624, 156, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 157의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (625, 157, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (626, 157, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (627, 157, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (628, 157, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 158의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (629, 158, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (630, 158, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (631, 158, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (632, 158, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 159의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (633, 159, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (634, 159, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (635, 159, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (636, 159, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 160의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (637, 160, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (638, 160, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (639, 160, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (640, 160, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 161의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (641, 161, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (642, 161, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (643, 161, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (644, 161, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 162의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (645, 162, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (646, 162, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (647, 162, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (648, 162, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 163의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (649, 163, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (650, 163, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (651, 163, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (652, 163, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 164의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (653, 164, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (654, 164, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (655, 164, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (656, 164, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 165의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (657, 165, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (658, 165, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (659, 165, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (660, 165, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 166의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (661, 166, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (662, 166, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (663, 166, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (664, 166, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 167의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (665, 167, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (666, 167, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (667, 167, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (668, 167, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 168의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (669, 168, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (670, 168, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (671, 168, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (672, 168, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 169의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (673, 169, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (674, 169, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (675, 169, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (676, 169, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 170의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (677, 170, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (678, 170, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (679, 170, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (680, 170, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 171의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (681, 171, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (682, 171, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (683, 171, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (684, 171, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 172의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (685, 172, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (686, 172, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (687, 172, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (688, 172, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 173의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (689, 173, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (690, 173, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (691, 173, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (692, 173, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 174의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (693, 174, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (694, 174, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (695, 174, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (696, 174, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 175의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (697, 175, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (698, 175, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (699, 175, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (700, 175, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 176의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (701, 176, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (702, 176, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (703, 176, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (704, 176, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 177의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (705, 177, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (706, 177, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (707, 177, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (708, 177, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 178의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (709, 178, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (710, 178, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (711, 178, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (712, 178, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 179의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (713, 179, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (714, 179, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (715, 179, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (716, 179, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 180의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (717, 180, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (718, 180, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (719, 180, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (720, 180, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 181의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (721, 181, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (722, 181, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (723, 181, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (724, 181, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 182의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (725, 182, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (726, 182, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (727, 182, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (728, 182, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 183의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (729, 183, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (730, 183, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (731, 183, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (732, 183, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 184의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (733, 184, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (734, 184, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (735, 184, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (736, 184, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 185의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (737, 185, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (738, 185, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (739, 185, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (740, 185, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 186의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (741, 186, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (742, 186, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (743, 186, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (744, 186, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 187의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (745, 187, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (746, 187, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (747, 187, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (748, 187, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 188의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (749, 188, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (750, 188, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (751, 188, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (752, 188, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 189의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (753, 189, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (754, 189, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (755, 189, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (756, 189, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 190의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (757, 190, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (758, 190, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (759, 190, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (760, 190, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 191의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (761, 191, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (762, 191, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (763, 191, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (764, 191, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 192의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (765, 192, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (766, 192, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (767, 192, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (768, 192, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 193의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (769, 193, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (770, 193, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (771, 193, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (772, 193, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 194의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (773, 194, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (774, 194, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (775, 194, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (776, 194, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 195의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (777, 195, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (778, 195, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (779, 195, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (780, 195, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 196의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (781, 196, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (782, 196, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (783, 196, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (784, 196, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 197의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (785, 197, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (786, 197, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (787, 197, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (788, 197, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 198의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (789, 198, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (790, 198, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (791, 198, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (792, 198, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 199의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (793, 199, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (794, 199, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (795, 199, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (796, 199, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 200의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (797, 200, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (798, 200, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (799, 200, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (800, 200, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 201의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (801, 201, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (802, 201, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (803, 201, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (804, 201, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 202의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (805, 202, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (806, 202, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (807, 202, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (808, 202, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 203의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (809, 203, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (810, 203, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (811, 203, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (812, 203, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 204의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (813, 204, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (814, 204, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (815, 204, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (816, 204, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 205의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (817, 205, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (818, 205, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (819, 205, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (820, 205, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 206의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (821, 206, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (822, 206, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (823, 206, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (824, 206, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 207의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (825, 207, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (826, 207, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (827, 207, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (828, 207, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 208의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (829, 208, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (830, 208, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (831, 208, '선택지 C', false, 2, '2계속오전 11:09026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (832, 208, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 209의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (833, 209, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (834, 209, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (835, 209, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (836, 209, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 210의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (837, 210, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (838, 210, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (839, 210, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (840, 210, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 211의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (841, 211, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (842, 211, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (843, 211, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (844, 211, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 212의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (845, 212, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (846, 212, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (847, 212, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (848, 212, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 213의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (849, 213, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (850, 213, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (851, 213, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (852, 213, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 214의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (853, 214, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (854, 214, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (855, 214, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (856, 214, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 215의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (857, 215, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (858, 215, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (859, 215, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (860, 215, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 216의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (861, 216, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (862, 216, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (863, 216, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (864, 216, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 217의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (865, 217, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (866, 217, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (867, 217, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (868, 217, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 218의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (869, 218, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (870, 218, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (871, 218, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (872, 218, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 219의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (873, 219, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (874, 219, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (875, 219, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (876, 219, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 220의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (877, 220, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (878, 220, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (879, 220, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (880, 220, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 221의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (881, 221, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (882, 221, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (883, 221, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (884, 221, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 222의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (885, 222, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (886, 222, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (887, 222, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (888, 222, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 223의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (889, 223, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (890, 223, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (891, 223, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (892, 223, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 224의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (893, 224, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (894, 224, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (895, 224, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (896, 224, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 225의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (897, 225, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (898, 225, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (899, 225, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (900, 225, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 226의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (901, 226, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (902, 226, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (903, 226, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (904, 226, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 227의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (905, 227, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (906, 227, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (907, 227, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (908, 227, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 228의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (909, 228, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (910, 228, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (911, 228, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (912, 228, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 229의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (913, 229, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (914, 229, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (915, 229, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (916, 229, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 230의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (917, 230, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (918, 230, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (919, 230, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (920, 230, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 231의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (921, 231, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (922, 231, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (923, 231, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (924, 231, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 232의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (925, 232, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (926, 232, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (927, 232, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (928, 232, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 233의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (929, 233, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (930, 233, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (931, 233, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (932, 233, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 234의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (933, 234, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (934, 234, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (935, 234, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (936, 234, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 235의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (937, 235, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (938, 235, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (939, 235, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (940, 235, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 236의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (941, 236, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (942, 236, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (943, 236, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (944, 236, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 237의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (945, 237, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (946, 237, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (947, 237, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (948, 237, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 238의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (949, 238, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (950, 238, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (951, 238, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (952, 238, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 239의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (953, 239, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (954, 239, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (955, 239, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (956, 239, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
-- Mission Question 240의 선택지
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (957, 240, '선택지 A', true, 0, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (958, 240, '선택지 B', false, 1, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (959, 240, '선택지 C', false, 2, '2026-01-21 10:00:00', '2026-01-21 10:00:00');
INSERT INTO public.choices (id, question_id, content, is_correct, order_index, created_at, updated_at)
VALUES (960, 240, '선택지 D', false, 3, '2026-01-21 10:00:00', '2026-01-21 10:00:00');