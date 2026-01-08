package kr.kiomn2.bigtraffic.interfaces.finance.dto.response;

import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CardResponse {

    private Long id;
    private String cardName;
    private String cardCompany;
    private String maskedCardNumber;
    private String lastFourDigits;
    private CardType cardType;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private BigDecimal usedAmount;
    private BigDecimal availableCredit;
    private Integer billingDay;
    private Boolean isDefault;
    private Boolean isActive;
    private String color;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CardResponse from(Card card, String maskedCardNumber) {
        return CardResponse.builder()
                .id(card.getId())
                .cardName(card.getCardName())
                .cardCompany(card.getCardCompany())
                .maskedCardNumber(maskedCardNumber)
                .lastFourDigits(card.getLastFourDigits())
                .cardType(card.getCardType())
                .balance(card.getBalance())
                .creditLimit(card.getCreditLimit())
                .usedAmount(card.getUsedAmount())
                .availableCredit(card.getAvailableCredit())
                .billingDay(card.getBillingDay())
                .isDefault(card.getIsDefault())
                .isActive(card.getIsActive())
                .color(card.getColor())
                .memo(card.getMemo())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .build();
    }
}
