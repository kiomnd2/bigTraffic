package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountUpdateRequest;
import lombok.Getter;

@Getter
public class UpdateBankAccountCommand {
    private final Long userId;
    private final Long accountId;
    private final String accountName;
    private final String color;
    private final String memo;

    private UpdateBankAccountCommand(Long userId, Long accountId, String accountName,
                                     String color, String memo) {
        this.userId = userId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.color = color;
        this.memo = memo;
    }

    public static UpdateBankAccountCommand from(Long userId, Long accountId, BankAccountUpdateRequest request) {
        return new UpdateBankAccountCommand(
                userId,
                accountId,
                request.getAccountName(),
                request.getColor(),
                request.getMemo()
        );
    }
}
