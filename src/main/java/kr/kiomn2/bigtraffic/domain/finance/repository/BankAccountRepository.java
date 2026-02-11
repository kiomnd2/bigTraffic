package kr.kiomn2.bigtraffic.domain.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {

    List<BankAccount> findByUserId(Long userId);

    Optional<BankAccount> findByIdAndUserId(Long id, Long userId);

    BankAccount save(BankAccount bankAccount);

    void delete(BankAccount bankAccount);

    long countByUserId(Long userId);

    void unsetDefaultForOtherAccounts(Long userId, Long accountId);

    List<BankAccount> findByDynamicConditions(Long userId, Boolean isActive);
}
