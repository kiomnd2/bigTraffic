package kr.kiomn2.bigtraffic.domain.finance.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class UpdateBalanceCommand {
    private final Long userId;
    private final Long accountId;
    private final BigDecimal balance;
}
