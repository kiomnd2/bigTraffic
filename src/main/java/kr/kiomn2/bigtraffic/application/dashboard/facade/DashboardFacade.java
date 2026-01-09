package kr.kiomn2.bigtraffic.application.dashboard.facade;

import kr.kiomn2.bigtraffic.application.dashboard.dto.DashboardResponse;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
     * - 계좌 목록
     * - 카드 목록
     * - 총 자산 계산 (계좌 잔액 + 체크카드 잔액)
     *
     * @param user 인증된 사용자
     * @return 대시보드 응답 데이터
     */
    public DashboardResponse getDashboard(User user) {
        log.info("대시보드 데이터 조회 시작 - userId: {}", user.getId());

        // 계좌 목록 조회
        GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
        BankAccountListResponse accounts = bankAccountService.getBankAccounts(accountQuery);
        log.debug("계좌 목록 조회 완료 - 계좌 수: {}, 총 잔액: {}",
                accounts.getAccounts().size(), accounts.getTotalBalance());

        // 카드 목록 조회
        GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
        CardListResponse cards = cardService.getCards(cardQuery);
        log.debug("카드 목록 조회 완료 - 카드 수: {}", cards.getCards().size());

        // 총 자산 계산: 계좌 잔액 + 체크카드 잔액
        BigDecimal totalAssets = calculateTotalAssets(accounts.getTotalBalance(), cards);
        log.info("대시보드 데이터 조회 완료 - userId: {}, 총 자산: {}", user.getId(), totalAssets);

        return DashboardResponse.of(
                totalAssets,
                accounts.getTotalBalance(),
                accounts.getAccounts(),
                cards.getCards(),
                user.getName(),
                user.getEmail()
        );
    }

    /**
     * 총 자산 계산
     * 계좌 잔액 + 체크카드 잔액의 합계
     *
     * @param accountBalance 계좌 총 잔액
     * @param cards 카드 목록
     * @return 총 자산
     */
    private BigDecimal calculateTotalAssets(BigDecimal accountBalance, CardListResponse cards) {
        BigDecimal debitCardBalance = cards.getCards().stream()
                .filter(card -> "DEBIT".equals(card.getCardType().toString()))
                .map(CardResponse::getBalance)
                .filter(balance -> balance != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return accountBalance.add(debitCardBalance);
    }
}
