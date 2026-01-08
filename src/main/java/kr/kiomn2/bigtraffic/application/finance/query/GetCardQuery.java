package kr.kiomn2.bigtraffic.application.finance.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetCardQuery {
    private final Long userId;
    private final Long cardId;
}
