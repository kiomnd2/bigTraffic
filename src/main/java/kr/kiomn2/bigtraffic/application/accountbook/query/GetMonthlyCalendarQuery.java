package kr.kiomn2.bigtraffic.application.accountbook.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 월별 캘린더 조회 Query
 */
@Getter
@AllArgsConstructor
public class GetMonthlyCalendarQuery {

    /**
     * 사용자 ID
     */
    private final Long userId;

    /**
     * 연도
     */
    private final int year;

    /**
     * 월 (1-12)
     */
    private final int month;
}
