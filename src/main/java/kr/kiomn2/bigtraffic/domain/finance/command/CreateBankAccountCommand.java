package kr.kiomn2.bigtraffic.domain.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreateBankAccountCommand {
    private final Long userId;
    private final String accountName;
    private final String bankName;
    private final String accountNumber;
    private final AccountType accountType;
    private final BigDecimal balance;
    private final String color;
    private final String memo;
}
