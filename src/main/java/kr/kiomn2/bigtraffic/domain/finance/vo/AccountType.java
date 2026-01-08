package kr.kiomn2.bigtraffic.domain.finance.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    CHECKING("보통예금"),
    SAVINGS("적금/예금"),
    INVESTMENT("투자/증권");

    private final String description;
}
