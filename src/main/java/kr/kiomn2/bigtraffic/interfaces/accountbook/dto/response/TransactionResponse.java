package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private Long categoryId;
    private String categoryName;
    private String description;
    private LocalDate transactionDate;
    private PaymentMethod paymentMethod;
    private Long accountId;
    private Long cardId;
    private String memo;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategoryId())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .paymentMethod(transaction.getPaymentMethod())
                .accountId(transaction.getAccountId())
                .cardId(transaction.getCardId())
                .memo(transaction.getMemo())
                .build();
    }

    public static TransactionResponse fromWithCategory(Transaction transaction, String categoryName) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategoryId())
                .categoryName(categoryName)
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .paymentMethod(transaction.getPaymentMethod())
                .accountId(transaction.getAccountId())
                .cardId(transaction.getCardId())
                .memo(transaction.getMemo())
                .build();
    }
}
