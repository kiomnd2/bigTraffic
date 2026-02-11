package kr.kiomn2.bigtraffic.interfaces.finance.mapper;

import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.service.FinanceDataEncryptor;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CardMapper {

    private final FinanceDataEncryptor encryptor;

    public CardResponse toResponse(Card card) {
        String masked = encryptor.maskCardNumber(card.getCardNumber());
        return CardResponse.from(card, masked);
    }

    public CardResponse toDetailResponse(Card card) {
        String decrypted = encryptor.decrypt(card.getCardNumber());
        String masked = encryptor.maskCardNumber(card.getCardNumber());
        return CardResponse.fromWithCardNumber(card, decrypted, masked);
    }

    public CardListResponse toListResponse(List<Card> cards) {
        List<CardResponse> cardResponses = cards.stream()
                .map(this::toResponse)
                .toList();

        return CardListResponse.builder()
                .cards(cardResponses)
                .totalCount(cardResponses.size())
                .build();
    }
}
