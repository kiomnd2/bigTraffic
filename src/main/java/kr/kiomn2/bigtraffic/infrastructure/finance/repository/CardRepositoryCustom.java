package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;

import java.util.List;

public interface CardRepositoryCustom {
    List<Card> findByDynamicConditions(Long userId, CardType cardType, Boolean isActive);
}
