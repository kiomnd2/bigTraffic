# 새 기능 추가 가이드

사용자가 요청한 기능을 아래 패턴에 따라 생성한다.
인자: $ARGUMENTS (예: "accountbook 예산설정", "finance 자동이체")

## 생성 순서

### 1단계: Domain (핵심 비즈니스)
- `domain/{도메인}/entity/{Entity}.java` - JPA Entity (@Entity, @Table, Lombok)
- `domain/{도메인}/vo/{VO}.java` - 필요 시 Value Object (enum 등)
- `domain/{도메인}/exception/{Exception}.java` - 도메인 예외 (BusinessException 상속)
- `domain/{도메인}/{도메인}Repository.java` - Repository 인터페이스

### 2단계: Infrastructure (기술 구현)
- `infrastructure/{도메인}/repository/{도메인}RepositoryImpl.java` - JpaRepository 구현체
- 필요 시 RepositoryCustom + QueryDSL 구현

### 3단계: Application (유즈케이스)
- `application/{도메인}/command/{동작}Command.java` - 쓰기 명령 DTO
- `application/{도메인}/query/{조회}Query.java` - 읽기 조건 DTO
- `application/{도메인}/service/{도메인}Service.java` - 비즈니스 로직 오케스트레이션
- 여러 도메인 조합 시: `application/{도메인}/facade/{기능}Facade.java`

### 4단계: Interfaces (외부 노출)
- `interfaces/{도메인}/dto/request/{기능}Request.java` - 요청 DTO (record 또는 class)
- `interfaces/{도메인}/dto/response/{기능}Response.java` - 응답 DTO
- `interfaces/{도메인}/api/{도메인}Controller.java` - REST API (`/api/v1/{리소스}`)
- `interfaces/{도메인}/web/{도메인}WebController.java` - Thymeleaf SSR (`/{도메인}/**`)

## 체크리스트
- [ ] domain이 application/infrastructure를 import하지 않는가?
- [ ] Repository 인터페이스가 domain에, 구현체가 infrastructure에 있는가?
- [ ] API URL이 `/api/v1/{리소스}` 패턴인가?
- [ ] Lombok(@Getter, @RequiredArgsConstructor 등) 활용했는가?
