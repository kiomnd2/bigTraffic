package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.kiomn2.bigtraffic.domain.finance.entity.QBankAccount.bankAccount;

@Repository
@RequiredArgsConstructor
public class BankAccountRepositoryImpl implements BankAccountRepository {

    private final BankAccountJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BankAccount> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<BankAccount> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return jpaRepository.save(bankAccount);
    }

    @Override
    public void delete(BankAccount bankAccount) {
        jpaRepository.delete(bankAccount);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public void unsetDefaultForOtherAccounts(Long userId, Long accountId) {
        jpaRepository.unsetDefaultForOtherAccounts(userId, accountId);
    }

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
