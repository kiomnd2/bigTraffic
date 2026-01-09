package kr.kiomn2.bigtraffic.application.accountbook.command;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.TransactionCreateRequest;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CreateTransactionCommand {
    private final Long userId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final Long categoryId;
    private final String description;
    private final LocalDate transactionDate;
    private final PaymentMethod paymentMethod;
    private final Long accountId;
    private final Long cardId;
    private final String memo;

    private CreateTransactionCommand(Long userId, TransactionType type, BigDecimal amount,
                                     Long categoryId, String description, LocalDate transactionDate,
                                     PaymentMethod paymentMethod, Long accountId, Long cardId, String memo) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.categoryId = categoryId;
        this.description = description;
        this.transactionDate = transactionDate;
        this.paymentMethod = paymentMethod;
        this.accountId = accountId;
        this.cardId = cardId;
        this.memo = memo;
    }

    public static CreateTransactionCommand from(Long userId, TransactionCreateRequest request) {
        return new CreateTransactionCommand(
                userId,
                request.getType(),
                request.getAmount(),
                request.getCategoryId(),
                request.getDescription(),
                request.getTransactionDate(),
                request.getPaymentMethod(),
                request.getAccountId(),
                request.getCardId(),
                request.getMemo()
        );
    }
}
