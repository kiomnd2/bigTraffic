package kr.kiomn2.bigtraffic.infrastructure.finance.repository;

import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountJpaRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUserId(Long userId);

    List<BankAccount> findByUserIdAndIsActive(Long userId, Boolean isActive);

    List<BankAccount> findByUserIdAndAccountType(Long userId, AccountType accountType);

    Optional<BankAccount> findByIdAndUserId(Long id, Long userId);

    Optional<BankAccount> findByUserIdAndIsDefault(Long userId, Boolean isDefault);

    @Modifying
    @Query("UPDATE BankAccount ba SET ba.isDefault = false WHERE ba.userId = :userId AND ba.id != :accountId")
    void unsetDefaultForOtherAccounts(@Param("userId") Long userId, @Param("accountId") Long accountId);

    boolean existsByUserIdAndIsDefault(Long userId, Boolean isDefault);

    long countByUserId(Long userId);
}
