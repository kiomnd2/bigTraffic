package kr.kiomn2.bigtraffic.infrastructure.accountbook.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static kr.kiomn2.bigtraffic.domain.accountbook.entity.QTransaction.transaction;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Transaction> findByDynamicConditions(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long accountId,
            Long cardId
    ) {
        return queryFactory
                .selectFrom(transaction)
                .where(
                        userIdEq(userId),
                        typeEq(type),
                        categoryIdEq(categoryId),
                        dateBetween(startDate, endDate),
                        accountIdEq(accountId),
                        cardIdEq(cardId)
                )
                .orderBy(transaction.transactionDate.desc(), transaction.createdAt.desc())
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? transaction.userId.eq(userId) : null;
    }

    private BooleanExpression typeEq(TransactionType type) {
        return type != null ? transaction.type.eq(type) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? transaction.categoryId.eq(categoryId) : null;
    }

    private BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return transaction.transactionDate.between(startDate, endDate);
        } else if (startDate != null) {
            return transaction.transactionDate.goe(startDate);
        } else if (endDate != null) {
            return transaction.transactionDate.loe(endDate);
        }
        return null;
    }

    private BooleanExpression accountIdEq(Long accountId) {
        return accountId != null ? transaction.accountId.eq(accountId) : null;
    }

    private BooleanExpression cardIdEq(Long cardId) {
        return cardId != null ? transaction.cardId.eq(cardId) : null;
    }

    @Override
    public Page<Transaction> findByDynamicConditionsWithPaging(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long accountId,
            Long cardId,
            Pageable pageable
    ) {
        List<Transaction> content = queryFactory
                .selectFrom(transaction)
                .where(
                        userIdEq(userId),
                        typeEq(type),
                        categoryIdEq(categoryId),
                        dateBetween(startDate, endDate),
                        accountIdEq(accountId),
                        cardIdEq(cardId)
                )
                .orderBy(transaction.transactionDate.desc(), transaction.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(transaction.count())
                .from(transaction)
                .where(
                        userIdEq(userId),
                        typeEq(type),
                        categoryIdEq(categoryId),
                        dateBetween(startDate, endDate),
                        accountIdEq(accountId),
                        cardIdEq(cardId)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
