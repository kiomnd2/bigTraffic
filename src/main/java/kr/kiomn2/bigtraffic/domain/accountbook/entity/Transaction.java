package kr.kiomn2.bigtraffic.domain.accountbook.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 가계부 거래 엔티티
 * 수입/지출 기록을 관리
 */
@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "memo", length = 1000)
    private String memo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateInfo(BigDecimal amount, Long categoryId, String description,
                          LocalDate transactionDate, PaymentMethod paymentMethod,
                          Long accountId, Long cardId, String memo) {
        if (amount != null) {
            this.amount = amount;
        }
        if (categoryId != null) {
            this.categoryId = categoryId;
        }
        if (description != null) {
            this.description = description;
        }
        if (transactionDate != null) {
            this.transactionDate = transactionDate;
        }
        if (paymentMethod != null) {
            this.paymentMethod = paymentMethod;
        }
        // accountId와 cardId는 null 허용
        this.accountId = accountId;
        this.cardId = cardId;
        if (memo != null) {
            this.memo = memo;
        }
    }

    public static Transaction create(Long userId, TransactionType type, BigDecimal amount,
                                    Long categoryId, String description, LocalDate transactionDate,
                                    PaymentMethod paymentMethod, Long accountId, Long cardId, String memo) {
        return Transaction.builder()
                .userId(userId)
                .type(type)
                .amount(amount)
                .categoryId(categoryId)
                .description(description)
                .transactionDate(transactionDate)
                .paymentMethod(paymentMethod)
                .accountId(accountId)
                .cardId(cardId)
                .memo(memo)
                .build();
    }
}
