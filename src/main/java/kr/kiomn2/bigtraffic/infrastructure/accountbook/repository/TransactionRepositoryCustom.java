package kr.kiomn2.bigtraffic.infrastructure.accountbook.repository;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;

import java.time.LocalDate;
import java.util.List;

/**
 * Transaction 동적 쿼리를 위한 Custom Repository
 */
public interface TransactionRepositoryCustom {

    /**
     * 동적 조건으로 거래 목록 조회
     *
     * @param userId 사용자 ID
     * @param type 거래 유형 (nullable)
     * @param categoryId 카테고리 ID (nullable)
     * @param startDate 시작 날짜 (nullable)
     * @param endDate 종료 날짜 (nullable)
     * @param accountId 계좌 ID (nullable)
     * @param cardId 카드 ID (nullable)
     * @return 거래 목록
     */
    List<Transaction> findByDynamicConditions(
            Long userId,
            TransactionType type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long accountId,
            Long cardId
    );
}
