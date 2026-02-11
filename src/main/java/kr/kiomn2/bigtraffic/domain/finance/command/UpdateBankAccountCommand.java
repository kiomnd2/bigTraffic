package kr.kiomn2.bigtraffic.domain.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateBankAccountCommand {
    private final Long userId;
    private final Long accountId;
    private final String accountName;
    private final String bankName;
    private final AccountType accountType;
    private final BigDecimal balance;
    private final Boolean isActive;
    private final String color;
    private final String memo;
}
