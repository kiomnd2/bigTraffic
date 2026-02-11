# 아키텍처 의존성 검증

프로젝트의 DDD 4-Layer 의존 방향 규칙 위반을 검사한다.

## 검증 규칙

### 규칙 1: domain → application 의존 금지
domain 패키지의 모든 Java 파일에서 `import kr.kiomn2.bigtraffic.application` 이 없어야 한다.

### 규칙 2: domain → infrastructure 의존 금지
domain 패키지의 모든 Java 파일에서 `import kr.kiomn2.bigtraffic.infrastructure` 가 없어야 한다.

### 규칙 3: application → interfaces 의존 금지
application 패키지의 모든 Java 파일에서 `import kr.kiomn2.bigtraffic.interfaces` 가 없어야 한다.

### 규칙 4: DIP - Repository 인터페이스 위치
Repository 인터페이스는 `domain/{도메인}/` 에 있어야 하고, 구현체는 `infrastructure/{도메인}/repository/` 에 있어야 한다.

## 검증 절차
1. Grep으로 각 규칙의 위반 import문 탐색
2. 위반 사항을 파일명:라인번호와 함께 보고
3. 수정 방안 제시

## 출력 형식
```
[PASS] 규칙 1: domain → application 의존 없음
[FAIL] 규칙 3: application → interfaces 의존 발견
  - application/dashboard/facade/DashboardFacade.java:10
    import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse
```
