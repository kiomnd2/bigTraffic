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
    private String cardNumber; // 실제 카드번호 (상세 페이지용)
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

    /**
     * 실제 카드번호를 포함한 상세 정보용 Response 생성
     */
    public static CardResponse fromWithCardNumber(Card card, String cardNumber, String maskedCardNumber) {
        return CardResponse.builder()
                .id(card.getId())
                .cardName(card.getCardName())
                .cardCompany(card.getCardCompany())
                .cardNumber(cardNumber) // 실제 번호 포함
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
