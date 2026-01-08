package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardCreateRequest;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
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

    private CreateCardCommand(Long userId, String cardName, String cardCompany,
                              String cardNumber, CardType cardType, BigDecimal balance,
                              BigDecimal creditLimit, Integer billingDay,
                              String color, String memo) {
        this.userId = userId;
        this.cardName = cardName;
        this.cardCompany = cardCompany;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.billingDay = billingDay;
        this.color = color;
        this.memo = memo;
    }

    public static CreateCardCommand from(Long userId, CardCreateRequest request) {
        return new CreateCardCommand(
                userId,
                request.getCardName(),
                request.getCardCompany(),
                request.getCardNumber(),
                request.getCardType(),
                request.getBalance(),
                request.getCreditLimit(),
                request.getBillingDay(),
                request.getColor(),
                request.getMemo()
        );
    }
}
