package kr.kiomn2.bigtraffic.domain.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;

import java.util.List;
import java.util.Optional;

public interface CardRepository {

    List<Card> findByUserId(Long userId);

    Optional<Card> findByIdAndUserId(Long id, Long userId);

    Card save(Card card);

    void delete(Card card);

    long countByUserId(Long userId);

    void unsetDefaultForOtherCards(Long userId, Long cardId);

    List<Card> findByDynamicConditions(Long userId, CardType cardType, Boolean isActive);
}
