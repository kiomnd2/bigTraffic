package kr.kiomn2.bigtraffic.domain.finance.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardType {
    CREDIT("신용카드"),
    DEBIT("체크카드"),
    CHECK("직불카드");

    private final String description;
}
