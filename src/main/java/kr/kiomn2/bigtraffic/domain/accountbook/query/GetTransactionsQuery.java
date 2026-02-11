package kr.kiomn2.bigtraffic.domain.accountbook.query;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetTransactionsQuery {
    private final Long userId;
    private final TransactionType type;
    private final Long categoryId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long accountId;
    private final Long cardId;
}
