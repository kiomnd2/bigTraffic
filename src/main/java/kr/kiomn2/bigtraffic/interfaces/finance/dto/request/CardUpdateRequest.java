package kr.kiomn2.bigtraffic.interfaces.finance.dto.request;

import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {

    private String cardName;

    private String cardCompany;

    private CardType cardType;

    private BigDecimal balance;

    private BigDecimal creditLimit;

    private Boolean isActive;

    private String color;

    private String memo;
}
