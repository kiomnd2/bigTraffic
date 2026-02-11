package kr.kiomn2.bigtraffic.domain.finance.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetBankAccountsQuery {
    private final Long userId;
    private final Boolean isActive;
}
