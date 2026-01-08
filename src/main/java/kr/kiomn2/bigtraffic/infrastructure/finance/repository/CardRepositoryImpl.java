package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.kiomn2.bigtraffic.domain.finance.entity.QCard.card;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Card> findByDynamicConditions(Long userId, CardType cardType, Boolean isActive) {
        return queryFactory
                .selectFrom(card)
                .where(
                        userIdEq(userId),
                        cardTypeEq(cardType),
                        isActiveEq(isActive)
                )
                .orderBy(card.createdAt.desc())
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? card.userId.eq(userId) : null;
    }

    private BooleanExpression cardTypeEq(CardType cardType) {
        return cardType != null ? card.cardType.eq(cardType) : null;
    }

    private BooleanExpression isActiveEq(Boolean isActive) {
        return isActive != null ? card.isActive.eq(isActive) : null;
    }
}
