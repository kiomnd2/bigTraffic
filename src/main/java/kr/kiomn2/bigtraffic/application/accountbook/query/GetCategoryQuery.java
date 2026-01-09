package kr.kiomn2.bigtraffic.application.accountbook.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCategoryQuery {
    private final Long userId;
    private final Long categoryId;
}
