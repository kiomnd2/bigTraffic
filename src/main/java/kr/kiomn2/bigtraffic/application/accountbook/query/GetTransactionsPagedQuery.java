package kr.kiomn2.bigtraffic.application.accountbook.query;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * 페이징된 거래 목록 조회 Query
 */
@Getter
@AllArgsConstructor
public class GetTransactionsPagedQuery {

    /**
     * 사용자 ID
     */
    private final Long userId;

    /**
     * 거래 유형
     */
    private final TransactionType type;

    /**
     * 카테고리 ID
     */
    private final Long categoryId;

    /**
     * 시작 날짜
     */
    private final LocalDate startDate;

    /**
     * 종료 날짜
     */
    private final LocalDate endDate;

    /**
     * 계좌 ID
     */
    private final Long accountId;

    /**
     * 카드 ID
     */
    private final Long cardId;

    /**
     * 페이징 정보
     */
    private final Pageable pageable;
}
