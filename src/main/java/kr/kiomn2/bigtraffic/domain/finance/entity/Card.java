package kr.kiomn2.bigtraffic.domain.finance.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.infrastructure.finance.security.CardNumberConverter;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "card_name", nullable = false, length = 100)
    private String cardName;

    @Column(name = "card_company", nullable = false, length = 100)
    private String cardCompany;

    @Convert(converter = CardNumberConverter.class)
    @Column(name = "card_number", nullable = false, length = 500)
    private String cardNumber;

    @Column(name = "last_four_digits", nullable = false, length = 4)
    private String lastFourDigits;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 20)
    private CardType cardType;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "used_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal usedAmount = BigDecimal.ZERO;

    @Column(name = "billing_day")
    private Integer billingDay;

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

    public void updateUsedAmount(BigDecimal amount) {
        this.usedAmount = amount;
    }

    public BigDecimal getAvailableCredit() {
        if (cardType == CardType.CREDIT && creditLimit != null) {
            return creditLimit.subtract(usedAmount != null ? usedAmount : BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetAsDefault() {
        this.isDefault = false;
    }

    public void updateInfo(String cardName, String color, String memo) {
        if (cardName != null) {
            this.cardName = cardName;
        }
        if (color != null) {
            this.color = color;
        }
        if (memo != null) {
            this.memo = memo;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public static Card create(String cardNumber, String lastFourDigits,
                              kr.kiomn2.bigtraffic.application.finance.command.CreateCardCommand command) {
        return Card.builder()
                .userId(command.getUserId())
                .cardName(command.getCardName())
                .cardCompany(command.getCardCompany())
                .cardNumber(cardNumber)
                .lastFourDigits(lastFourDigits)
                .cardType(command.getCardType())
                .balance(command.getBalance())
                .creditLimit(command.getCreditLimit())
                .usedAmount(BigDecimal.ZERO)
                .billingDay(command.getBillingDay())
                .isDefault(false)
                .isActive(true)
                .color(command.getColor())
                .memo(command.getMemo())
                .build();
    }
}
