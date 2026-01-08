package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountCreateRequest {

    @NotBlank(message = "계좌 이름은 필수입니다.")
    private String accountName;

    @NotBlank(message = "은행명은 필수입니다.")
    private String bankName;

    @NotBlank(message = "계좌번호는 필수입니다.")
    private String accountNumber;

    @NotNull(message = "계좌 유형은 필수입니다.")
    private AccountType accountType;

    private BigDecimal balance;

    private String color;

    private String memo;
}
