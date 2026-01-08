package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;

import java.util.List;

public interface BankAccountRepositoryCustom {
    List<BankAccount> findByDynamicConditions(Long userId, Boolean isActive);
}
