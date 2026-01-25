package kr.kiomn2.bigtraffic.domain.finance.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.application.finance.command.CreateBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBankAccountCommand;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.infrastructure.finance.security.AccountNumberConverter;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Convert(converter = AccountNumberConverter.class)
    @Column(name = "account_number", nullable = false, length = 500)
    private String accountNumber;

    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @Column(name = "balance", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(length = 7)
    private String color;

    @Column(length = 500)
    private String memo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetAsDefault() {
        this.isDefault = false;
    }

    public void updateInfo(String accountName, String color, String memo) {
        if (accountName != null) {
            this.accountName = accountName;
        }
        if (color != null) {
            this.color = color;
        }
        if (memo != null) {
            this.memo = memo;
        }
    }

    public void updateFullInfo(UpdateBankAccountCommand command) {
        if (command.getAccountName() != null) {
            this.accountName = command.getAccountName();
        }
        if (command.getBankName() != null) {
            this.bankName = command.getBankName();
        }
        if (command.getAccountType() != null) {
            this.accountType = command.getAccountType();
        }
        if (command.getBalance() != null) {
            this.balance = command.getBalance();
        }
        if (command.getIsActive() != null) {
            this.isActive = command.getIsActive();
        }
        if (command.getColor() != null) {
            this.color = command.getColor();
        }
        if (command.getMemo() != null) {
            this.memo = command.getMemo();
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public static BankAccount create(String accountNumber, String lastFourDigits, CreateBankAccountCommand command) {
        return BankAccount.builder()
                .userId(command.getUserId())
                .accountName(command.getAccountName())
                .bankName(command.getBankName())
                .accountNumber(accountNumber)
                .lastFourDigits(lastFourDigits)
                .accountType(command.getAccountType())
                .balance(command.getBalance() != null ? command.getBalance() : BigDecimal.ZERO)
                .isDefault(false)
                .isActive(true)
                .color(command.getColor())
                .memo(command.getMemo())
                .build();
    }
}
