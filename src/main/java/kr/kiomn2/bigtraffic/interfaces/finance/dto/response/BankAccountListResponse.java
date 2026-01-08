package kr.kiomn2.bigtraffic.interfaces.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BankAccountListResponse {

    private List<BankAccountResponse> accounts;
    private BigDecimal totalBalance;
    private Integer totalCount;
}
