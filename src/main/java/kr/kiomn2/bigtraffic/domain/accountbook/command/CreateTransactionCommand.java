package kr.kiomn2.bigtraffic.domain.accountbook.command;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
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
}
