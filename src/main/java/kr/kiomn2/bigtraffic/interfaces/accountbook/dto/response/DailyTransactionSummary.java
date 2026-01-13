package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 일별 거래 요약 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class DailyTransactionSummary {

    /**
     * 날짜
     */
    private LocalDate date;

    /**
     * 거래 건수
     */
    private int transactionCount;

    /**
     * 일별 수입 합계
     */
    private BigDecimal dailyIncome;

    /**
     * 일별 지출 합계
     */
    private BigDecimal dailyExpense;

    /**
     * 일별 순액 (수입 - 지출)
     */
    private BigDecimal dailyNet;
}
