package kr.kiomn2.bigtraffic.domain.finance.service;

import kr.kiomn2.bigtraffic.domain.finance.command.*;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.exception.CardNotFoundException;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.domain.finance.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final FinanceDataEncryptor encryptor;

    @Transactional
    public Card createCard(CreateCardCommand command) {
        String lastFourDigits = encryptor.extractLastFourDigits(command.getCardNumber());

        Card card = Card.create(
                command.getCardNumber(),
                lastFourDigits,
                command
        );

        if (cardRepository.countByUserId(command.getUserId()) == 0) {
            card.setAsDefault();
        }

        return cardRepository.save(card);
    }

    public List<Card> getCards(GetCardsQuery query) {
        return cardRepository.findByDynamicConditions(
                query.getUserId(),
                query.getCardType(),
                query.getIsActive()
        );
    }

    public Card getCard(GetCardQuery query) {
        return getCardById(query.getUserId(), query.getCardId());
    }

    @Transactional
    public Card updateCard(UpdateCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());
        card.updateFullInfo(command);
        return card;
    }

    @Transactional
    public void deleteCard(DeleteCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());

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
    public Card setDefaultCard(SetDefaultCardCommand command) {
        Card card = getCardById(command.getUserId(), command.getCardId());

        cardRepository.unsetDefaultForOtherCards(command.getUserId(), command.getCardId());
        card.setAsDefault();

        return card;
    }

    private Card getCardById(Long userId, Long cardId) {
        return cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다."));
    }
}
