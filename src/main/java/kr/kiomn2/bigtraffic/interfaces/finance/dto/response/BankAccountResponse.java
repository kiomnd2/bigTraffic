package kr.kiomn2.bigtraffic.interfaces.finance.dto.response;

import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BankAccountResponse {

    private Long id;
    private String accountName;
    private String bankName;
    private String maskedAccountNumber;
    private String lastFourDigits;
    private AccountType accountType;
    private BigDecimal balance;
    private Boolean isDefault;
    private Boolean isActive;
    private String color;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BankAccountResponse from(BankAccount account, String maskedAccountNumber) {
        return BankAccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .bankName(account.getBankName())
                .maskedAccountNumber(maskedAccountNumber)
                .lastFourDigits(account.getLastFourDigits())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .isDefault(account.getIsDefault())
                .isActive(account.getIsActive())
                .color(account.getColor())
                .memo(account.getMemo())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
