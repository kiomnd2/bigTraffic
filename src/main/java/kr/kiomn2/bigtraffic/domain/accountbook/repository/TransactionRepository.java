package kr.kiomn2.bigtraffic.domain.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    Transaction save(Transaction transaction);

    void delete(Transaction transaction);

    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByDynamicConditions(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long accountId,
            Long cardId
    );

    Page<Transaction> findByDynamicConditionsWithPaging(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long accountId,
            Long cardId,
            Pageable pageable
    );
}
