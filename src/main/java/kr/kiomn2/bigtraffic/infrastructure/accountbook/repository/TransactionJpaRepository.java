package kr.kiomn2.bigtraffic.infrastructure.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {

    /**
     * 사용자 ID로 거래 목록 조회
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * 사용자 ID와 거래 유형으로 조회
     */
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    /**
     * 사용자 ID와 거래 ID로 조회
     */
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    /**
     * 사용자 ID와 카테고리 ID로 조회
     */
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    /**
     * 사용자 ID와 기간으로 조회
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    List<Transaction> findByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 사용자 ID, 거래 유형, 기간으로 조회
     */
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = :type " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    List<Transaction> findByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 계좌 ID로 거래 조회
     */
    List<Transaction> findByUserIdAndAccountId(Long userId, Long accountId);

    /**
     * 카드 ID로 거래 조회
     */
    List<Transaction> findByUserIdAndCardId(Long userId, Long cardId);
}
