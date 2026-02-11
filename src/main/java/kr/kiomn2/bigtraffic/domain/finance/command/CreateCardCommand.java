package kr.kiomn2.bigtraffic.domain.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CreateCardCommand {
    private final Long userId;
    private final String cardName;
    private final String cardCompany;
    private final String cardNumber;
    private final CardType cardType;
    private final BigDecimal balance;
    private final BigDecimal creditLimit;
    private final Integer billingDay;
    private final String color;
    private final String memo;
}
