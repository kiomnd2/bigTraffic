package kr.kiomn2.bigtraffic.application.accountbook.query;

import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoriesQuery {
    private final Long userId;
    private final TransactionType type;
    private final Boolean isActive;
}
