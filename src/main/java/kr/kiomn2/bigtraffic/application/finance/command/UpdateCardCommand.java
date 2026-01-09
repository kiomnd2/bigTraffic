package kr.kiomn2.bigtraffic.application.finance.command;

import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.CardUpdateRequest;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateCardCommand {
    private final Long userId;
    private final Long cardId;
    private final String cardName;
    private final String cardCompany;
    private final CardType cardType;
    private final BigDecimal balance;
    private final BigDecimal creditLimit;
    private final Boolean isActive;
    private final String color;
    private final String memo;

    private UpdateCardCommand(Long userId, Long cardId, String cardName,
                              String cardCompany, CardType cardType, BigDecimal balance,
                              BigDecimal creditLimit, Boolean isActive, String color, String memo) {
        this.userId = userId;
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardCompany = cardCompany;
        this.cardType = cardType;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.isActive = isActive;
        this.color = color;
        this.memo = memo;
    }

    public static UpdateCardCommand from(Long userId, Long cardId, CardUpdateRequest request) {
        return new UpdateCardCommand(
                userId,
                cardId,
                request.getCardName(),
                request.getCardCompany(),
                request.getCardType(),
                request.getBalance(),
                request.getCreditLimit(),
                request.getIsActive(),
                request.getColor(),
                request.getMemo()
        );
    }
}
