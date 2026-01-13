package kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 월별 캘린더 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class MonthlyCalendarResponse {

    /**
     * 연도
     */
    private int year;

    /**
     * 월 (1-12)
     */
    private int month;

    /**
     * 일별 거래 요약 목록
     */
    private List<DailyTransactionSummary> dailySummaries;

    /**
     * 월별 수입 합계
     */
    private BigDecimal monthlyIncome;

    /**
     * 월별 지출 합계
     */
    private BigDecimal monthlyExpense;

    /**
     * 월별 순액 (수입 - 지출)
     */
    private BigDecimal monthlyNet;
}
