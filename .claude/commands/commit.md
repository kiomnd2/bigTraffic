# 커밋 메시지 작성

git 변경사항을 분석하여 프로젝트 컨벤션에 맞는 커밋 메시지를 작성한다.

## 커밋 메시지 형식

```
{타입}: {도메인} - {변경 요약}

{상세 설명 (선택)}
```

## 타입 규칙

| 타입 | 사용 시점 | 예시 |
|------|----------|------|
| `feat` | 새로운 기능 추가 | feat: AccountBook - 예산 설정 기능 추가 |
| `fix` | 버그 수정 | fix: Finance - 계좌번호 복호화 오류 수정 |
| `refactor` | 리팩토링 (기능 변경 없음) | refactor: Auth - JWT 제거, 세션 기반 전환 |
| `style` | UI/UX 변경, 코드 스타일 | style: Dashboard - 대시보드 레이아웃 변경 |
| `chore` | 설정, 빌드, 의존성 | chore: Docker MySQL 설정 추가 |
| `docs` | 문서 변경 | docs: CLAUDE.md 아키텍처 섹션 업데이트 |
| `test` | 테스트 추가/수정 | test: Finance - 카드 서비스 단위 테스트 추가 |

## 도메인 태그
변경된 파일의 패키지 경로에서 도메인을 추출한다:
- `auth/` → Auth
- `finance/` → Finance
- `accountbook/` → AccountBook
- `accountgroup/` → AccountGroup
- `dashboard/` → Dashboard
- `config/`, `common/` → Common
- 여러 도메인에 걸친 경우 → 주요 도메인 또는 생략

## 절차
1. `git status`로 변경 파일 확인
2. `git diff --staged` 또는 `git diff`로 변경 내용 분석
3. 변경 파일의 도메인과 타입 판별
4. 커밋 메시지 초안 작성
5. 사용자 확인 후 커밋

## 한국어 작성 규칙
- 요약은 한국어로 간결하게 (50자 이내)
- 상세 설명이 필요하면 본문에 작성
- 기존 커밋 히스토리 스타일과 일관성 유지

## 예시

```
feat: AccountBook - 월별 캘린더 조회 기능 추가

- GetMonthlyCalendarQuery 추가
- TransactionRepositoryCustom에 월별 조회 쿼리 구현
- 캘린더 웹 페이지 연동
```

```
fix: Finance - ajax 호출 시 계좌 잔액 업데이트 오류 수정
```

```
refactor: Auth - OAuth 로그인 플로우 패키지 구조 변경

- OauthUserProcessor 인터페이스 분리
- infrastructure/auth/security/oauth/process/ 하위로 구현체 이동
```
