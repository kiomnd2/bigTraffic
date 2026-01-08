package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.kiomn2.bigtraffic.domain.finance.entity.QBankAccount.bankAccount;

@RequiredArgsConstructor
public class BankAccountRepositoryImpl implements BankAccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BankAccount> findByDynamicConditions(Long userId, Boolean isActive) {
        return queryFactory
                .selectFrom(bankAccount)
                .where(
                        userIdEq(userId),
                        isActiveEq(isActive)
                )
                .orderBy(bankAccount.createdAt.desc())
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? bankAccount.userId.eq(userId) : null;
    }

    private BooleanExpression isActiveEq(Boolean isActive) {
        return isActive != null ? bankAccount.isActive.eq(isActive) : null;
    }
}
