---

# PROJECT: SNS-AI-BACKEND

## 0. 개발자 정보
- 백엔드 취업 준비 중인 개발 입문자(초보)
- Java·Spring 학습 병행 중
- 코드 설명을 항상 한국어로 해줄 것

## 1. 서비스 개요
*   **대상:** 소상공인 대상 AI SNS 콘텐츠 자동 생성 SaaS.
*   **기능:** 업종·키워드 입력 → Gemini API → 인스타 캡션·해시태그·블로그 초안 생성.
*   **모델:** Free(월 5회) / Pro(월 9,900원·무제한) 구독 모델.

---

## 2. 기술 스택

| 영역 | 기술 | 버전 |
| :--- | :--- | :--- |
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.3.x |
| **ORM** | Spring Data JPA + Hibernate | - |
| **DB** | PostgreSQL (Docker) | 16 |
| **Auth** | Spring Security + JWT | - |
| **AI** | Gemini API | gemini-2.0-flash |
| **Build** | Gradle Groovy DSL | 9.4.1 |
| **Docs** | SpringDoc OpenAPI | - |
| **Deploy** | Railway | - |

---

## 3. 로컬 환경

*   **DB host:** `localhost:5432`
*   **DB name:** `myapp`
*   **DB user:** `myuser`
*   **DB password:** `mypassword`
*   **Server port:** `8080`
*   **Docker DB:**
    *   시작: `docker start my-postgres`
    *   정지: `docker stop my-postgres`
*   **실행:** `./gradlew bootRun`
*   **빌드:** `./gradlew build -x test`

---

## 4. 패키지 구조

```text
com.snsai.backend
├── domain
│   ├── user
│   │   ├── controller   (UserController.java)
│   │   ├── service      (UserService.java)
│   │   ├── repository   (UserRepository.java)
│   │   ├── entity       (User.java)
│   │   └── dto          (SignupRequest / LoginRequest / UserResponse)
│   ├── content
│   │   ├── controller   (ContentController.java)
│   │   ├── service      (ContentService.java)
│   │   ├── repository   (ContentRepository.java)
│   │   ├── entity       (Content.java)
│   │   └── dto          (ContentRequest / ContentResponse)
│   └── subscription
│       ├── controller   (SubscriptionController.java)
│       ├── service      (SubscriptionService.java)
│       ├── repository   (SubscriptionRepository.java)
│       ├── entity       (Subscription.java)
│       └── dto          (SubscriptionRequest / SubscriptionResponse)
└── global
    ├── config
    │   ├── SecurityConfig.java       (JWT 필터 등록, 경로별 인가)
    │   ├── JwtConfig.java            (토큰 생성·검증)
    │   └── SwaggerConfig.java
    ├── exception
    │   ├── GlobalExceptionHandler.java  (@RestControllerAdvice)
    │   ├── CustomException.java
    │   └── ErrorCode.java            (enum: 에러코드·메시지·HTTP상태)
    └── common
        └── ApiResponse.java          (공통 응답 래퍼)
```

---

## 5. DB 스키마

```sql
-- users
id            BIGINT PK AUTO_INCREMENT
email         VARCHAR(100) UNIQUE NOT NULL
password      VARCHAR(255) NOT NULL          -- BCrypt
nickname      VARCHAR(50) NOT NULL
role          VARCHAR(20) DEFAULT 'FREE'     -- FREE | PRO
usage_count   INT DEFAULT 0                  -- 이번 달 사용 횟수
created_at    TIMESTAMP
updated_at    TIMESTAMP

-- contents
id            BIGINT PK AUTO_INCREMENT
user_id       BIGINT FK → users.id
industry      VARCHAR(50)                    -- 업종 (카페, 식당 등)
keywords      TEXT                           -- 입력 키워드
result_text   TEXT                           -- 생성된 콘텐츠
content_type  VARCHAR(20)                    -- INSTAGRAM | BLOG
created_at    TIMESTAMP

-- subscriptions
id            BIGINT PK AUTO_INCREMENT
user_id       BIGINT FK → users.id UNIQUE
plan          VARCHAR(20)                    -- FREE | PRO
status        VARCHAR(20)                    -- ACTIVE | CANCELLED
started_at    TIMESTAMP
expired_at    TIMESTAMP
payment_key   VARCHAR(255)                   -- 토스 페이먼츠 키
```

---

## 6. API 엔드포인트

### [인증 - 토큰 불필요]
*   `POST /api/v1/auth/signup` : 회원가입
*   `POST /api/v1/auth/login` : 로그인 → access_token 반환

### [콘텐츠 - 토큰 필요]
*   `POST /api/v1/contents` : AI 콘텐츠 생성
*   `GET /api/v1/contents` : 내 생성 이력 목록
*   `GET /api/v1/contents/{id}` : 단건 조회
*   `DELETE /api/v1/contents/{id}` : 삭제

### [사용자]
*   `GET /api/v1/users/me` : 내 정보 조회
*   `PATCH /api/v1/users/me` : 정보 수정

### [구독]
*   `POST /api/v1/subscriptions` : 구독 결제 (토스 페이먼츠)
*   `DELETE /api/v1/subscriptions` : 구독 해지
*   `GET /api/v1/subscriptions/me` : 내 구독 상태

---

## 7. 공통 응답 형식

```json
// 성공
{ 
  "success": true, 
  "data": { ... } 
}

// 실패
{ 
  "success": false, 
  "error": { 
    "code": "USER_NOT_FOUND", 
    "message": "사용자를 찾을 수 없습니다." 
  } 
}
```

---

## 8. 코딩 규칙 (반드시 준수)

*   **아키텍처:** Controller → Service → Repository (계층 간 직접 호출 금지)
*   **DTO:** Entity를 Controller에 절대 노출 금지. 요청/응답 DTO 분리
*   **예외:** `throw new CustomException(ErrorCode.XXX)` 형태로 통일
*   **응답:** `ApiResponse.success(data)` / `ApiResponse.fail(errorCode)` 사용
*   **Lombok:** `@RequiredArgsConstructor`(생성자 주입), `@Getter`, `@Builder` 사용
*   **JPA:** `ddl-auto=update` (운영 시 none), N+1 문제 주의 → `fetch join` 사용
*   **보안:** 비밀번호 BCrypt, JWT secret 환경변수로 관리, SQL Injection 주의
*   **Git 커밋 컨벤션:** 
    *   `feat`: 새로운 기능 추가
    *   `fix`: 버그 수정
    *   `docs`: 문서 수정
    *   `style`: 코드 포맷팅, 세미콜론 누락 등 (코드 변경 없음)
    *   `refactor`: 코드 리팩토링
    *   `test`: 테스트 코드 추가 및 수정
    *   `chore`: 빌드 업무, 설정 등

---

## 9. JWT 인증 흐름

1.  로그인 → `access_token(1h)` + `refresh_token(7d)` 발급
2.  요청 시 Header: `Authorization: Bearer {access_token}`
3.  만료 시 `POST /api/v1/auth/refresh` 로 재발급
4.  `SecurityConfig`에서 `/api/v1/auth/**` 는 `permitAll`, 나머지는 `authenticated`

---

## 10. Gemini API 연동 방식

*   **Endpoint:** `[https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent](https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent)`
*   **Key:** 환경변수 `GEMINI_API_KEY`
*   **Prompt:** `"당신은 SNS 마케팅 전문가입니다. 업종: {industry}, 키워드: {keywords} 기반으로 인스타그램 캡션 3가지와 해시태그 10개를 생성해주세요."`
*   **호출 위치:** `ContentService.generateContent()`
*   **제한 처리:** FREE 사용자 월 5회 초과 시 `CustomException(ErrorCode.USAGE_LIMIT_EXCEEDED)` 발생

---

## 11. 구현 현황

*   [x] 프로젝트 초기 세팅 (Spring Boot 3.3, Gradle, Java 21)
*   [x] Docker PostgreSQL 연결
*   [x] application.properties DB 설정
*   [x] 깃 & 깃허브 리포지토리 연결
*   [x] Hello World API
*   [x] 공통 응답 래퍼 `ApiResponse<T>`
*   [x] 글로벌 예외 처리 `GlobalExceptionHandler`
*   [ ] 회원가입 / 로그인 (JWT)
*   [ ] 콘텐츠 생성 API + Gemini 연동
*   [ ] 구독 결제 (토스 페이먼츠)
*   [ ] GitHub Actions CI/CD
*   [ ] Railway 배포

---

## 12. 코드 생성 요청 규칙

1.  파일 단위로 전체 코드 제공 (일부 발췌 금지)
2.  `import` 문 포함
3.  핵심 포인트 3줄 이내 한국어 설명
4.  보안 이슈 있으면 반드시 언급
5.  설명은 한국어, 코드 주석은 최소화
6.  **버전 업데이트 시 Git 커밋 메시지 추천 및 `13. 버전 및 변경 이력` 업데이트 수행할 것**
7.  **기능 추가 시 반드시 구현 방식, 사용 기술/로직, 그리고 '왜 그렇게 구현했는지(이유)'를 초보자 눈높이에서 자세히 설명할 것.**

---

## 13. 버전 및 변경 이력 (Changelog)

| 날짜 | 버전 | 변경 사항 | 비고 |
| :--- | :--- | :--- | :--- |
| 2024-XX-XX | v0.0.1 | - 프로젝트 초기 세팅<br>- Docker PostgreSQL 연결<br>- 깃/깃허브 리포지토리 연동 | |
| 2024-05-02 | v0.0.2 | - 서버 정상 작동 테스트용 Hello API 추가<br>- Spring Security 기본 설정(Hello API 허용) 추가 | |
| 2024-05-02 | v0.0.3 | - 공통 응답 래퍼(ApiResponse) 클래스 추가 | |
| 2024-05-02 | v0.0.4 | - 글로벌 예외 처리(GlobalExceptionHandler) 구축<br>- SecurityConfig 테스트 API 허용 | |
