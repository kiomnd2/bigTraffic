package kr.kiomn2.bigtraffic.application.dashboard.facade;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.domain.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.domain.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 대시보드 Facade
 * 여러 도메인 서비스를 조합하여 대시보드 데이터를 제공하는 중간 진입점
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardFacade {

    private final BankAccountService bankAccountService;
    private final CardService cardService;

    /**
     * 사용자 대시보드 데이터 조회
     *
     * @param user 인증된 사용자
     * @return 대시보드 데이터 (도메인 엔티티 기반)
     */
    public DashboardData getDashboard(User user) {
        log.info("대시보드 데이터 조회 시작 - userId: {}", user.getId());

        // 계좌 목록 조회
        GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
        List<BankAccount> accounts = bankAccountService.getBankAccounts(accountQuery);

        BigDecimal totalBalance = accounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.debug("계좌 목록 조회 완료 - 계좌 수: {}, 총 잔액: {}", accounts.size(), totalBalance);

        // 카드 목록 조회
        GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
        List<Card> cards = cardService.getCards(cardQuery);
        log.debug("카드 목록 조회 완료 - 카드 수: {}", cards.size());

        // 총 자산 계산: 계좌 잔액 + 체크카드 잔액
        BigDecimal totalAssets = calculateTotalAssets(totalBalance, cards);
        log.info("대시보드 데이터 조회 완료 - userId: {}, 총 자산: {}", user.getId(), totalAssets);

        return new DashboardData(totalAssets, totalBalance, accounts, cards, user.getName(), user.getEmail());
    }

    private BigDecimal calculateTotalAssets(BigDecimal accountBalance, List<Card> cards) {
        BigDecimal debitCardBalance = cards.stream()
                .filter(card -> card.getCardType() == CardType.DEBIT)
                .map(Card::getBalance)
                .filter(balance -> balance != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return accountBalance.add(debitCardBalance);
    }

    @Getter
    @RequiredArgsConstructor
    public static class DashboardData {
        private final BigDecimal totalAssets;
        private final BigDecimal totalBalance;
        private final List<BankAccount> accounts;
        private final List<Card> cards;
        private final String username;
        private final String email;
    }
}
