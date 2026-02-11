# AccountBook 도메인 상세 지식

## 개요
카테고리 기반 수입/지출 가계부 관리 도메인

## 핵심 구성요소

### Entity
- `Transaction` - 거래 내역 (금액, 날짜, 메모, 카테고리, 결제수단)
- `Category` - 거래 카테고리 (이름, 아이콘, 사용자별)

### VO
- `TransactionType` - 거래 유형 (수입/지출 enum)
- `PaymentMethod` - 결제 수단 (enum)

## 주요 기능
- 카테고리 CRUD (사용자별 커스텀 카테고리)
- 거래 내역 CRUD
- 월별 캘린더 조회 (`GetMonthlyCalendarQuery`)
- 페이징 조회 (`GetTransactionsPagedQuery`)

## 패키지 위치
```
interfaces/accountbook/dto/request/{CategoryCreate,CategoryUpdate,TransactionCreate,TransactionUpdate}Request.java
interfaces/accountbook/dto/response/{CategoryResponse,TransactionResponse}.java
application/accountbook/service/{CategoryService,TransactionService}.java
application/accountbook/command/{Create,Update,Delete}{Category,Transaction}Command.java
application/accountbook/query/{GetCategories,GetCategory,GetTransaction,GetTransactions,GetMonthlyCalendar,GetTransactionsPaged}Query.java
domain/accountbook/entity/{Transaction,Category}.java
domain/accountbook/vo/{TransactionType,PaymentMethod}.java
infrastructure/accountbook/repository/{CategoryRepository,TransactionRepository,TransactionRepositoryCustom}.java
```
