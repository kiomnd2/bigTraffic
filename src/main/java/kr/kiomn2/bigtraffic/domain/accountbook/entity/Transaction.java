package kr.kiomn2.bigtraffic.domain.accountbook.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.application.accountbook.command.CreateTransactionCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.UpdateTransactionCommand;
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

    public void updateInfo(UpdateTransactionCommand command) {
        if (command.getAmount() != null) {
            this.amount = command.getAmount();
        }
        if (command.getCategoryId() != null) {
            this.categoryId = command.getCategoryId();
        }
        if (command.getDescription() != null) {
            this.description = command.getDescription();
        }
        if (command.getTransactionDate() != null) {
            this.transactionDate = command.getTransactionDate();
        }
        if (command.getPaymentMethod() != null) {
            this.paymentMethod = command.getPaymentMethod();
        }
        // accountId와 cardId는 null 허용
        this.accountId = command.getAccountId();
        this.cardId = command.getCardId();
        if (command.getMemo() != null) {
            this.memo = command.getMemo();
        }
    }

    public static Transaction create(CreateTransactionCommand command) {
        return Transaction.builder()
                .userId(command.getUserId())
                .type(command.getType())
                .amount(command.getAmount())
                .categoryId(command.getCategoryId())
                .description(command.getDescription())
                .transactionDate(command.getTransactionDate())
                .paymentMethod(command.getPaymentMethod())
                .accountId(command.getAccountId())
                .cardId(command.getCardId())
                .memo(command.getMemo())
                .build();
    }
}
