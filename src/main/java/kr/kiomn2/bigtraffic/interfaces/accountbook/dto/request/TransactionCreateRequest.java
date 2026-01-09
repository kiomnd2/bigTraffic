package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequest {

    private TransactionType type;
    private BigDecimal amount;
    private Long categoryId;
    private String description;
    private LocalDate transactionDate;
    private PaymentMethod paymentMethod;
    private Long accountId;
    private Long cardId;
    private String memo;
}
