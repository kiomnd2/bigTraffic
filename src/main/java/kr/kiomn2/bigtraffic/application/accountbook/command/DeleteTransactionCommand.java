package kr.kiomn2.bigtraffic.application.accountbook.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteTransactionCommand {
    private final Long userId;
    private final Long transactionId;
}
