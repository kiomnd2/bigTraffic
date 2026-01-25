package kr.kiomn2.bigtraffic.application.finance.service;

import kr.kiomn2.bigtraffic.application.finance.command.CreateCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.DeleteCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.SetDefaultCardCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateCardCommand;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.exception.CardNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.finance.repository.CardRepository;
import kr.kiomn2.bigtraffic.infrastructure.finance.security.FinanceDataEncryptor;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final FinanceDataEncryptor encryptor;

    @Transactional
    public CardResponse createCard(CreateCardCommand command) {
        String lastFourDigits = encryptor.extractLastFourDigits(command.getCardNumber());

        Card card = Card.create(
                command.getCardNumber(),
                lastFourDigits,
                command
        );

        // 첫 번째 카드인 경우 자동으로 기본 카드로 설정
        if (cardRepository.countByUserId(command.getUserId()) == 0) {
            card.setAsDefault();
        }

        Card savedCard = cardRepository.save(card);
        String maskedCardNumber = getMaskedCardNumber(savedCard);
        return CardResponse.from(savedCard, maskedCardNumber);
    }

    public CardListResponse getCards(GetCardsQuery query) {
        // QueryDSL 동적 쿼리 사용
        List<Card> cards = cardRepository.findByDynamicConditions(
                query.getUserId(),
                query.getCardType(),
                query.getIsActive()
        );

        List<CardResponse> cardResponses = cards.stream()
                .map(card -> CardResponse.from(
                        card,
                        getMaskedCardNumber(card)
                ))
                .toList();

        return CardListResponse.builder()
                .cards(cardResponses)
                .totalCount(cardResponses.size())
                .build();
    }

    public CardResponse getCard(GetCardQuery query) {
        Card card = getCardById(query.getUserId(), query.getCardId());
        String decryptedCardNumber = encryptor.decrypt(card.getCardNumber());
        String maskedCardNumber = getMaskedCardNumber(card);
        // 상세 조회 시에는 실제 카드번호 포함
        return CardResponse.fromWithCardNumber(card, decryptedCardNumber, maskedCardNumber);
    }

    @Transactional
    public CardResponse updateCard(UpdateCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());
        card.updateFullInfo(command);

        String maskedCardNumber = getMaskedCardNumber(card);
        return CardResponse.from(card, maskedCardNumber);
    }

    @Transactional
    public void deleteCard(DeleteCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());

        // 기본 카드를 삭제하는 경우, 다른 카드를 기본으로 설정
        if (card.getIsDefault()) {
            List<Card> otherCards = cardRepository.findByUserId(command.getUserId()).stream()
                    .filter(c -> !c.getId().equals(command.getCardId()))
                    .filter(Card::getIsActive)
                    .toList();

            if (!otherCards.isEmpty()) {
                otherCards.get(0).setAsDefault();
            }
        }

        cardRepository.delete(card);
    }

    @Transactional
    public CardResponse setDefaultCard(SetDefaultCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());

        // 다른 카드의 기본 설정 해제
        cardRepository.unsetDefaultForOtherCards(command.getUserId(), command.getCardId());

        // 현재 카드를 기본으로 설정
        card.setAsDefault();

        String maskedCardNumber = getMaskedCardNumber(card);
        return CardResponse.from(card, maskedCardNumber);
    }

    private Card getCardById(Long userId, Long cardId) {
        return cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다."));
    }

    private String getMaskedCardNumber(Card card) {
        return encryptor.maskCardNumber(card.getCardNumber());
    }
}
