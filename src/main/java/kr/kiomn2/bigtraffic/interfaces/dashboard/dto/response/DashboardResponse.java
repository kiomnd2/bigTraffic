package kr.kiomn2.bigtraffic.interfaces.dashboard.dto.response;

import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private BigDecimal totalAssets;
    private BigDecimal totalBalance;
    private int accountCount;
    private int cardCount;
    private List<BankAccountResponse> accounts;
    private List<CardResponse> cards;
    private String username;
    private String email;

    public static DashboardResponse of(
            BigDecimal totalAssets,
            BigDecimal totalBalance,
            List<BankAccountResponse> accounts,
            List<CardResponse> cards,
            String username,
            String email
    ) {
        return DashboardResponse.builder()
                .totalAssets(totalAssets)
                .totalBalance(totalBalance)
                .accountCount(accounts.size())
                .cardCount(cards.size())
                .accounts(accounts)
                .cards(cards)
                .username(username)
                .email(email)
                .build();
    }
}
