package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.repository.CardRepository;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.kiomn2.bigtraffic.domain.finance.entity.QCard.card;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final CardJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Card> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Card> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public Card save(Card card) {
        return jpaRepository.save(card);
    }

    @Override
    public void delete(Card cardEntity) {
        jpaRepository.delete(cardEntity);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public void unsetDefaultForOtherCards(Long userId, Long cardId) {
        jpaRepository.unsetDefaultForOtherCards(userId, cardId);
    }

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
