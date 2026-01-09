package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountUpdateRequest {

    private String accountName;

    private String bankName;

    private AccountType accountType;

    private BigDecimal balance;

    private Boolean isActive;

    private String color;

    private String memo;
}
