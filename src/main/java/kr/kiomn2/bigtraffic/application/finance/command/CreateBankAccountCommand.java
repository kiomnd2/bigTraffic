package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountCreateRequest;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateBankAccountCommand {
    private final Long userId;
    private final String accountName;
    private final String bankName;
    private final String accountNumber;
    private final AccountType accountType;
    private final BigDecimal balance;
    private final String color;
    private final String memo;

    private CreateBankAccountCommand(Long userId, String accountName, String bankName,
                                     String accountNumber, AccountType accountType,
                                     BigDecimal balance, String color, String memo) {
        this.userId = userId;
        this.accountName = accountName;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.color = color;
        this.memo = memo;
    }

    public static CreateBankAccountCommand from(Long userId, BankAccountCreateRequest request) {
        return new CreateBankAccountCommand(
                userId,
                request.getAccountName(),
                request.getBankName(),
                request.getAccountNumber(),
                request.getAccountType(),
                request.getBalance(),
                request.getColor(),
                request.getMemo()
        );
    }
}
