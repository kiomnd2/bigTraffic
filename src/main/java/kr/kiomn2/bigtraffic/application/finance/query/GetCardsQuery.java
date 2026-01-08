package kr.kiomn2.bigtraffic.application.finance.query;

import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetCardsQuery {
    private final Long userId;
    private final CardType cardType;
    private final Boolean isActive;
}
