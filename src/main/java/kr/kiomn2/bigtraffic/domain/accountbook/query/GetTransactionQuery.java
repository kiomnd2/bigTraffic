package kr.kiomn2.bigtraffic.domain.accountbook.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTransactionQuery {
    private final Long userId;
    private final Long transactionId;
}
