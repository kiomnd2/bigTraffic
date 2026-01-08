package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateRequest {

    @NotNull(message = "잔액은 필수입니다.")
    private BigDecimal balance;
}
