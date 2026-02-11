package kr.kiomn2.bigtraffic.domain.accountbook.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMonthlyCalendarQuery {
    private final Long userId;
    private final int year;
    private final int month;
}
