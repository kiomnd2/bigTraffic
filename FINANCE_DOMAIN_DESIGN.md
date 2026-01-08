# 가계부 - 금융자산 도메인 설계

**작성일**: 2026-01-08 | **도메인**: Finance (계좌/카드 관리)

---

## 개요

BigTraffic을 가계부 애플리케이션으로 전환. 사용자의 계좌 및 카드 정보를 등록하고 관리하는 기능 구현.

**설계 방향**
- BankAccount와 Card 별도 엔티티 분리
- 계좌번호/카드번호 AES-256 암호화
- 잔액 관리 포함

---

## 엔티티 설계

### BankAccount (계좌)
```java
@Entity
@Table(name = "bank_accounts")
- id: Long (PK)
- userId: Long (FK)
- accountName: String (사용자 정의 이름)
- bankName: String (은행명)
- accountNumber: String (암호화)
- lastFourDigits: String (마지막 4자리)
- accountType: AccountType (CHECKING, SAVINGS, INVESTMENT)
- balance: BigDecimal
- isDefault: Boolean
- isActive: Boolean
- color: String
- memo: String
- createdAt, updatedAt: LocalDateTime
```

### Card (카드)
```java
@Entity
@Table(name = "cards")
- id: Long (PK)
- userId: Long (FK)
- cardName: String (사용자 정의 이름)
- cardCompany: String (카드사)
- cardNumber: String (암호화)
- lastFourDigits: String
- cardType: CardType (CREDIT, DEBIT, CHECK)
- balance: BigDecimal (체크카드)
- creditLimit: BigDecimal (신용카드)
- usedAmount: BigDecimal (신용카드)
- billingDay: Integer (1-31)
- isDefault: Boolean
- isActive: Boolean
- color: String
- memo: String
- createdAt, updatedAt: LocalDateTime
```

---

## 패키지 구조

```
kr.kiomn2.bigtraffic.domain.finance
├─ entity (BankAccount, Card)
├─ vo (AccountType, CardType)
└─ exception

kr.kiomn2.bigtraffic.application.finance
└─ service (BankAccountService, CardService)

kr.kiomn2.bigtraffic.infrastructure.finance
├─ repository (BankAccountRepository, CardRepository)
├─ security (FinanceDataEncryptor, Converters)
└─ config (EncryptionConfig)

kr.kiomn2.bigtraffic.interfaces.finance
├─ api (BankAccountController, CardController)
└─ dto (Request/Response)
```

---

## API 엔드포인트

### 계좌
```
POST   /api/v1/bank-accounts           - 등록
GET    /api/v1/bank-accounts           - 목록
GET    /api/v1/bank-accounts/{id}      - 상세
PUT    /api/v1/bank-accounts/{id}      - 수정
DELETE /api/v1/bank-accounts/{id}      - 삭제
PATCH  /api/v1/bank-accounts/{id}/default - 기본 설정
PATCH  /api/v1/bank-accounts/{id}/balance - 잔액 업데이트
```

### 카드
```
POST   /api/v1/cards           - 등록
GET    /api/v1/cards           - 목록
GET    /api/v1/cards/{id}      - 상세
PUT    /api/v1/cards/{id}      - 수정
DELETE /api/v1/cards/{id}      - 삭제
PATCH  /api/v1/cards/{id}/default - 기본 설정
```

### 통합
```
GET    /api/v1/finance/summary - 전체 자산 요약
```

---

## 데이터베이스 스키마

```sql
CREATE TABLE bank_accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(500) NOT NULL,
    last_four_digits CHAR(4) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0,
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    color VARCHAR(7),
    memo VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

CREATE TABLE cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    card_name VARCHAR(100) NOT NULL,
    card_company VARCHAR(100) NOT NULL,
    card_number VARCHAR(500) NOT NULL,
    last_four_digits CHAR(4) NOT NULL,
    card_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2),
    credit_limit DECIMAL(15,2),
    used_amount DECIMAL(15,2) DEFAULT 0,
    billing_day INTEGER,
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    color VARCHAR(7),
    memo VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    CHECK (billing_day IS NULL OR (billing_day >= 1 AND billing_day <= 31))
);
```

---

## 보안 구현

**암호화**: AES-256-GCM
- 키: 환경변수 관리 (`FINANCE_ENCRYPTION_KEY`)
- JPA AttributeConverter로 자동 암호화/복호화
- 응답 마스킹: 계좌 `110-***-***789`, 카드 `****-****-****-3456`

---

## 구현 순서

1. Enum (AccountType, CardType)
2. 암호화 유틸리티 (FinanceDataEncryptor, Converters)
3. 엔티티 (BankAccount, Card)
4. 예외 클래스
5. Repository
6. Service (CRUD, 기본 설정, 잔액 관리)
7. DTO
8. Controller
9. 테스트

---

## 기술 스택

Spring Boot 3.5.8 | Java 21 | MySQL 5.7 | JPA | Spring Security | AES-256-GCM
