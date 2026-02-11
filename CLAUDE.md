# CLAUDE.md

## 1. 프로젝트 개요
카카오 OAuth 기반 개인 가계부 관리 웹 어플리케이션
Spring Boot 3.5.8 + Java 21 + Thymeleaf SSR + Gradle (Kotlin DSL)

## 2. 아키텍처 (DDD 4-Layer)

```
interfaces/       → Controller, DTO (Request/Response)
application/      → Facade, Command, Query, Service
domain/           → Entity, Value Object, Service, Repository(interface)
infrastructure/   → Repository(구현체), Config, Security
```

## 3. 의존 방향 (절대 규칙)
- interfaces → application or domain
- application → domain ← infrastructure
- domain에서 application/infrastructure를 절대 의존하지 않는다
- DIP: domain/{도메인}/{도메인}Repository(인터페이스) ← infrastructure/{도메인}/repository/{구현체}

## 4. 새 기능 추가 시 패키지 위치
- Controller: `interfaces/{도메인}/api/`
- Web Controller: `interfaces/{도메인}/web/`
- DTO: `interfaces/{도메인}/dto/request/`, `interfaces/{도메인}/dto/response/`
- Facade: `application/{도메인}/facade/`
- Command/Query: `application/{도메인}/command/`, `application/{도메인}/query/`
- Service: `application/{도메인}/service/`
- Entity: `domain/{도메인}/entity/`
- VO: `domain/{도메인}/vo/`
- Exception: `domain/{도메인}/exception/`
- Repository: `infrastructure/{도메인}/repository/`

## 5. API URL 패턴
- REST API: `/api/v1/{리소스}`
- 웹페이지: `/{도메인}/**`

## 6. 도메인 목록
Auth, Finance, AccountBook, AccountGroup
(상세 지식은 `/domain-auth`, `/domain-finance`, `/domain-accountbook`, `/domain-accountgroup` 참조)

## 7. 변경 보고서
- 모든 변경점에 대한 보고서를 작성하고 report 디렉토리에 report_yyyymmddHHmmss.md 파일로 작성한다

## 8. Skills 안내
- `/add-feature` - 새 기능 추가 가이드
- `/domain-auth`, `/domain-finance`, `/domain-accountbook`, `/domain-accountgroup` - 도메인별 상세 지식
- `/build` - 빌드/실행/Docker 명령어
- `/check-arch` - 의존성 검증
- `/commit` - 커밋 메시지 작성
