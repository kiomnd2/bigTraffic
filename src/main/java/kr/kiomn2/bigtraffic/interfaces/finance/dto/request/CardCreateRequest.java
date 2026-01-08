package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardCreateRequest {

    @NotBlank(message = "카드 이름은 필수입니다.")
    private String cardName;

    @NotBlank(message = "카드사는 필수입니다.")
    private String cardCompany;

    @NotBlank(message = "카드번호는 필수입니다.")
    private String cardNumber;

    @NotNull(message = "카드 유형은 필수입니다.")
    private CardType cardType;

    private BigDecimal balance;

    private BigDecimal creditLimit;

    @Min(value = 1, message = "결제일은 1~31 사이여야 합니다.")
    @Max(value = 31, message = "결제일은 1~31 사이여야 합니다.")
    private Integer billingDay;

    private String color;

    private String memo;
}
