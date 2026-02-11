# Auth 도메인 상세 지식

## 개요
카카오 OAuth 2.0 기반 인증/인가 도메인

## 핵심 구성요소

### Entity
- `User` - 사용자 (email, name, role, 카카오 연동 정보)
- `BlacklistedToken` - 로그아웃된 JWT 토큰

### VO
- `Role` - 사용자 권한 (enum)

### Exception
- `UserNotFoundException`
- `InvalidPasswordException`
- `DuplicateEmailException`
- `BusinessException` (공통 상위 예외)
- `ErrorCode` (에러 코드 enum)

## 인증 플로우
1. 카카오 OAuth 2.0 로그인 → `OAuth2AuthenticationSuccessHandler`
2. `OauthUserProcessor`로 사용자 생성/조회
3. JWT 토큰 발급 + 세션 기반 인증 병행
4. 로그아웃 시 토큰 블랙리스트 등록

## 주요 서비스
- `AuthService` - 회원탈퇴, 사용자 정보 조회
- `CustomUserDetailsService` - Spring Security UserDetails 구현

## 패키지 위치
```
interfaces/auth/api/AuthController.java
interfaces/auth/web/LoginController.java
application/auth/service/AuthService.java
application/auth/command/{Logout,ProcessKakaoLogin,Withdrawal}Command.java
application/auth/query/{GetCurrentUser,ValidateToken}Query.java
domain/auth/entity/{User,BlacklistedToken}.java
infrastructure/auth/security/oauth/
infrastructure/auth/repository/{UserRepository,BlacklistedTokenRepository}.java
```
