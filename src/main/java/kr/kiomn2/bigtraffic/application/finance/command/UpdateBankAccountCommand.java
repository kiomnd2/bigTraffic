package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountUpdateRequest;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
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

    private UpdateBankAccountCommand(Long userId, Long accountId, String accountName,
                                     String bankName, AccountType accountType, BigDecimal balance,
                                     Boolean isActive, String color, String memo) {
        this.userId = userId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.bankName = bankName;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = isActive;
        this.color = color;
        this.memo = memo;
    }

    public static UpdateBankAccountCommand from(Long userId, Long accountId, BankAccountUpdateRequest request) {
        return new UpdateBankAccountCommand(
                userId,
                accountId,
                request.getAccountName(),
                request.getBankName(),
                request.getAccountType(),
                request.getBalance(),
                request.getIsActive(),
                request.getColor(),
                request.getMemo()
        );
    }
}
