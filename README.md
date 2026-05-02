# SNS-AI-service
업종과 키워드만으로 인스타그램/블로그 콘텐츠를 자동 생성해주는 AI SaaS 백엔드

## 1. 프로젝트 개요
소상공인들의 SNS 마케팅 업무를 자동화하기 위한 AI 기반 콘텐츠 생성 서비스입니다. 사용자가 자신의 업종과 원하는 키워드를 입력하면, Gemini 2.0 Flash 모델이 인스타그램 캡션, 해시태그, 블로그 초안 등을 맞춤형으로 생성해 제공합니다.

## 2. 주요 기능
*   **회원가입 및 인증**: JWT 기반의 안전한 회원가입 및 로그인 기능 (Access Token / Refresh Token)
*   **AI 콘텐츠 생성**: 사용자의 입력 데이터(업종, 키워드)를 바탕으로 Gemini API를 활용한 마케팅 텍스트 생성
*   **구독 시스템**: Free 플랜(월 5회 무료) 및 Pro 플랜(월 9,900원 무제한, 토스페이먼츠 연동 예정)

## 3. 기술 스택
*   **Language**: Java 21
*   **Framework**: Spring Boot 3.3.4
*   **Database**: PostgreSQL 16 (Docker)
*   **ORM**: Spring Data JPA
*   **Security**: Spring Security, JWT (JJWT)
*   **AI**: Google Gemini API (gemini-2.0-flash)
*   **Build**: Gradle

## 4. 로컬 실행 방법
1. **데이터베이스 실행 (Docker)**
   ```bash
   docker start my-postgres
   # 처음 실행 시: docker run --name my-postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=mypassword -e POSTGRES_DB=myapp -p 5432:5432 -d postgres:16
   ```
2. **애플리케이션 실행**
   * 터미널: `./gradlew bootRun`
   * 또는 IDE에서 `BackendApplication` 실행

## 5. API 명세 (일부)
자세한 API 스펙은 추후 Swagger를 통해 제공될 예정입니다.
* `POST /api/v1/auth/signup` - 회원가입
* `POST /api/v1/auth/login` - 로그인
* `POST /api/v1/contents` - AI 콘텐츠 생성 (예정)

## 6. 문의 및 개발자 정보
* 취업을 준비하는 주니어 백엔드 개발자의 토이 프로젝트입니다.
