# Big Traffic

대규모 트래픽 처리를 위한 Spring Boot 기반 OAuth 2.0 인증 시스템

## 프로젝트 개요

Big Traffic은 OAuth 2.0 기반의 소셜 로그인 인증 시스템을 구현한 Spring Boot 애플리케이션입니다. 현재 카카오 OAuth 로그인을 지원하며, JWT 토큰 기반의 인증/인가 체계를 갖추고 있습니다. DDD(Domain-Driven Design) 원칙을 적용한 레이어드 아키텍처로 설계되어 확장성과 유지보수성을 고려하였습니다.

## 기술 스택

- **Framework**: Spring Boot 3.5.8
- **Language**: Java 21
- **Build Tool**: Gradle 8.x (Kotlin DSL)
- **Database**: MySQL 5.7 (Production), H2 (Test)
- **Security**: Spring Security + OAuth2 Client
- **Authentication**: JWT (jjwt 0.12.5)
- **Template Engine**: Thymeleaf
- **API Documentation**: SpringDoc OpenAPI 2.7.0
- **HTTP Client**: Spring WebFlux
- **DevTools**: Lombok, Spring Boot DevTools

## 아키텍처

### 레이어드 아키텍처

프로젝트는 DDD 원칙에 따라 다음과 같이 구성됩니다:

```
kr.kiomn2.bigtraffic/
│
├── application/          # 애플리케이션 서비스 계층 (Use Case)
│   └── auth/
│       └── service/      # 비즈니스 로직 처리
│
├── domain/              # 도메인 모델 계층
│   └── auth/
│       ├── entity/      # 도메인 엔티티
│       └── exception/   # 도메인 예외
│
├── infrastructure/      # 인프라 계층
│   ├── auth/
│   │   ├── repository/  # 데이터 접근 계층
│   │   └── security/    # 보안 설정 및 필터
│   ├── config/          # 애플리케이션 설정
│   └── exception/       # 글로벌 예외 처리
│
└── interfaces/          # 인터페이스 계층 (프레젠테이션)
    └── auth/
        ├── api/         # REST API 컨트롤러
        ├── dto/         # 데이터 전송 객체
        └── web/         # 웹 페이지 컨트롤러
```

### 주요 계층 설명

- **Application Layer**: 비즈니스 유스케이스 구현, 도메인 객체 조정
- **Domain Layer**: 핵심 비즈니스 로직과 엔티티, 비즈니스 규칙 정의
- **Infrastructure Layer**: 기술적 구현(DB, 보안, 설정 등)
- **Interface Layer**: 외부와의 통신(REST API, 웹 페이지)

## 도메인 설계

### 1. Auth 도메인

현재 프로젝트의 핵심 도메인으로, 사용자 인증 및 권한 관리를 담당합니다.

#### 엔티티

##### User (사용자)
- **역할**: OAuth 기반 사용자 정보 관리
- **특징**: `UserDetails` 및 `OAuth2User` 인터페이스 구현
- **주요 속성**:
  - `id`: 사용자 고유 식별자 (PK)
  - `email`: 이메일 주소 (unique)
  - `username`: 사용자명 (unique)
  - `provider`: OAuth 제공자 (kakao)
  - `providerId`: OAuth 제공자의 사용자 ID
  - `profileUrl`: 프로필 이미지 URL
  - `lastLoginDate`: 최근 로그인 시간
  - `createdAt`, `updatedAt`: 생성/수정 시간

##### BlacklistedToken (블랙리스트 토큰)
- **역할**: 로그아웃 및 회원탈퇴한 사용자의 JWT 토큰 무효화
- **특징**: 만료된 토큰 자동 정리 스케줄러 연동
- **주요 속성**:
  - `id`: 토큰 ID (PK)
  - `token`: JWT 토큰 문자열 (unique)
  - `email`: 토큰 소유자 이메일
  - `expirationDate`: 토큰 만료 시간
  - `blacklistedAt`: 블랙리스트 등록 시간
  - `reason`: 등록 사유 (LOGOUT, WITHDRAWAL)

#### 서비스

##### AuthService
- 회원탈퇴 처리
- 사용자 및 관련 토큰 삭제

##### KakaoAuthService
- 카카오 OAuth 로그인 플로우 관리
- 인가 코드로 액세스 토큰 교환
- 카카오 사용자 정보 조회
- 사용자 생성/업데이트 및 JWT 토큰 발급

##### JwtBlacklistService
- JWT 토큰 블랙리스트 관리
- 토큰 유효성 검증
- 만료된 블랙리스트 토큰 자동 삭제 (매일 자정 실행)

#### 리포지토리

##### UserRepository
- 사용자 데이터 접근
- 이메일, OAuth 제공자 정보로 조회

##### BlacklistedTokenRepository
- 블랙리스트 토큰 관리
- 만료된 토큰 일괄 삭제 기능

### 도메인 관계도

```
┌─────────────────────────────────────────────────────┐
│                   Auth Domain                       │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────┐         ┌────────────────────┐  │
│  │    User      │         │ BlacklistedToken   │  │
│  ├──────────────┤         ├────────────────────┤  │
│  │ id           │         │ id                 │  │
│  │ email        │◄───────│ email (FK)         │  │
│  │ username     │         │ token              │  │
│  │ provider     │         │ expirationDate     │  │
│  │ providerId   │         │ blacklistedAt      │  │
│  │ profileUrl   │         │ reason             │  │
│  │ lastLoginDate│         └────────────────────┘  │
│  │ createdAt    │                                 │
│  │ updatedAt    │                                 │
│  └──────────────┘                                 │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## 주요 기능

### 1. 카카오 OAuth 2.0 로그인

- 카카오 인가 서버를 통한 소셜 로그인
- 신규 사용자 자동 등록
- 기존 사용자 로그인 시간 업데이트

### 2. JWT 기반 인증/인가

- Access Token 발급 (유효기간: 24시간)
- HS256 알고리즘 사용
- Authorization 헤더를 통한 토큰 전달
- 토큰 유효성 검증 필터

### 3. 토큰 블랙리스트

- 로그아웃 시 토큰 무효화
- 회원탈퇴 시 토큰 무효화
- 스케줄러를 통한 만료 토큰 자동 삭제

### 4. 보안

- Spring Security 적용
- CSRF 비활성화 (Stateless)
- 세션 비사용 정책
- 공개/보호 엔드포인트 구분

## 시작하기

### 필수 요구사항

- Java 21
- Gradle 8.x
- Docker & Docker Compose (MySQL 실행용)
- Kakao Developers 애플리케이션 등록 (OAuth 클라이언트 ID/Secret)

### 환경 설정

#### 1. 카카오 OAuth 설정

Kakao Developers(https://developers.kakao.com)에서 애플리케이션을 생성하고 다음 정보를 확인:

- REST API 키 (Client ID)
- Client Secret (보안 설정에서 활성화)
- Redirect URI: `http://localhost:8080/login/oauth2/code/kakao`

#### 2. 환경 변수 설정

`src/main/resources/application.properties` 또는 환경 변수 설정:

```properties
# Kakao OAuth
spring.security.oauth2.client.registration.kakao.client-id=YOUR_KAKAO_CLIENT_ID
spring.security.oauth2.client.registration.kakao.client-secret=YOUR_KAKAO_CLIENT_SECRET

# JWT Secret
jwt.secret=YOUR_JWT_SECRET_KEY_MINIMUM_32_CHARACTERS
```

#### 3. 데이터베이스 설정

Docker Compose로 MySQL 실행:

```bash
cd docker
docker-compose up -d
```

MySQL 설정 정보:
- Host: localhost:13306
- Database: kiomnd2-db
- Username: kiomnd2
- Password: hikmnd2

### 빌드 및 실행

#### Gradle을 사용한 실행

```bash
# 빌드
./gradlew build

# 테스트 제외 빌드
./gradlew build -x test

# 애플리케이션 실행
./gradlew bootRun
```

#### JAR 파일 실행

```bash
# JAR 빌드
./gradlew bootJar

# 실행
java -jar build/libs/bigTraffic-0.0.1.jar
```

### 접속 정보

- 애플리케이션: http://localhost:8080
- 로그인 페이지: http://localhost:8080/login
- API 문서 (Swagger): http://localhost:8080/swagger-ui.html
- H2 콘솔 (개발 모드): http://localhost:8080/h2-console

## API 엔드포인트

### 인증 관련

| Method | Endpoint | Description | 인증 필요 |
|--------|----------|-------------|----------|
| POST | `/api/auth/kakao/callback` | 카카오 로그인 콜백 처리 | No |
| GET | `/api/auth/validate` | JWT 토큰 유효성 검증 | No |
| GET | `/api/auth/me` | 현재 사용자 정보 조회 | Yes |
| POST | `/api/auth/logout` | 로그아웃 (토큰 무효화) | Yes |
| DELETE | `/api/auth/withdrawal` | 회원탈퇴 | Yes |

### 웹 페이지

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/login` | 로그인 페이지 |
| GET | `/user-info` | 사용자 정보 페이지 |
| GET | `/api/auth/kakao/callback` | 카카오 콜백 페이지 |

## OAuth 로그인 플로우

```
1. 사용자가 /login 페이지 접속
      ↓
2. "카카오로 시작하기" 버튼 클릭
      ↓
3. 카카오 인가 서버로 리다이렉트
      ↓
4. 사용자 동의 후 인가 코드 발급
      ↓
5. /api/auth/kakao/callback으로 리다이렉트 (code 포함)
      ↓
6. KakaoAuthService가 인가 코드로 액세스 토큰 요청
      ↓
7. 액세스 토큰으로 카카오 사용자 정보 조회
      ↓
8. DB에 사용자 저장/업데이트
      ↓
9. JWT 토큰 발급
      ↓
10. /user-info 페이지로 리다이렉트 (JWT 토큰 전달)
```

## 테스트

### 단위 테스트 실행

```bash
./gradlew test
```

### 특정 테스트 실행

```bash
./gradlew test --tests "kr.kiomn2.bigtraffic.BigTrafficApplicationTests"
```

### 테스트 커버리지 리포트

```bash
./gradlew test jacocoTestReport
```

## 배포

### Docker 이미지 빌드

```bash
# JAR 빌드
./gradlew bootJar

# Docker 이미지 빌드 (Dockerfile 작성 필요)
docker build -t bigtraffic:latest .
```

## 프로젝트 구조

```
bigTraffic/
├── src/
│   ├── main/
│   │   ├── java/kr/kiomn2/bigtraffic/
│   │   │   ├── application/        # 애플리케이션 서비스
│   │   │   ├── domain/             # 도메인 모델
│   │   │   ├── infrastructure/     # 인프라 계층
│   │   │   └── interfaces/         # 인터페이스 계층
│   │   └── resources/
│   │       ├── static/             # 정적 리소스
│   │       ├── templates/          # Thymeleaf 템플릿
│   │       └── application.properties
│   └── test/
│       └── java/kr/kiomn2/bigtraffic/
├── docker/
│   └── docker-compose.yml          # MySQL 설정
├── build.gradle.kts                # Gradle 빌드 설정
└── README.md
```

## 보안 고려사항

### JWT Secret 관리

- 프로덕션 환경에서는 반드시 환경 변수로 관리
- 최소 32자 이상의 안전한 랜덤 문자열 사용
- 주기적인 Secret 교체 권장

### OAuth Client Secret

- 절대 소스 코드에 하드코딩하지 말 것
- 환경 변수 또는 암호화된 설정 파일 사용

### HTTPS 사용

- 프로덕션 환경에서는 반드시 HTTPS 사용
- 카카오 Redirect URI도 HTTPS로 설정

## 향후 계획

- [ ] 추가 OAuth 제공자 지원 (Google, Naver 등)
- [ ] Refresh Token 구현
- [ ] Redis를 활용한 블랙리스트 캐싱
- [ ] 사용자 프로필 관리 기능
- [ ] 관리자 기능 추가
- [ ] 비즈니스 도메인 확장

## 라이선스

이 프로젝트는 개인 학습 및 포트폴리오 목적으로 제작되었습니다.

## 문의

프로젝트 관련 문의사항은 이슈를 등록해 주세요.
