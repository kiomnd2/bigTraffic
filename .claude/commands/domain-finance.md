# Finance 도메인 상세 지식

## 개요
은행 계좌 및 카드 관리 도메인

## 핵심 구성요소

### Entity
- `BankAccount` - 은행 계좌 (계좌번호 AES-256 암호화, 잔액, 은행명)
- `Card` - 카드 (카드번호 AES-256 암호화, 카드사, 타입)

### VO
- `AccountType` - 계좌 유형 (enum)
- `CardType` - 카드 유형 (CREDIT/DEBIT enum)

### Exception
- `BankAccountNotFoundException`
- `CardNotFoundException`
- `DuplicateFinanceAssetException`

## 보안
- AES-256-GCM 암호화: `FinanceDataEncryptor`
- JPA AttributeConverter로 자동 암복호화
  - `AccountNumberConverter` - 계좌번호
  - `CardNumberConverter` - 카드번호
- 암호화 설정: `EncryptionConfig`

## 주요 기능
- 계좌/카드 CRUD
- 기본 계좌/카드 설정 (SetDefault)
- 잔액 업데이트 (UpdateBalance)
- QueryDSL을 통한 커스텀 쿼리 (RepositoryCustom + Impl)

## 패키지 위치
```
interfaces/finance/api/{BankAccountController,CardController}.java
interfaces/finance/web/FinanceWebController.java
application/finance/service/{BankAccountService,CardService}.java
application/finance/command/{Create,Update,Delete,SetDefault}{BankAccount,Card}Command.java
application/finance/query/Get{BankAccount,Card}{,s}Query.java
domain/finance/entity/{BankAccount,Card}.java
infrastructure/finance/repository/{BankAccount,Card}Repository{,Custom,Impl}.java
infrastructure/finance/security/{AccountNumber,CardNumber}Converter.java
```
