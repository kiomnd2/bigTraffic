package kr.kiomn2.bigtraffic.interfaces.finance.web;

import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceWebController {

    private final BankAccountService bankAccountService;
    private final CardService cardService;

//    @GetMapping("/user-info")
//    public String userInfo() {
//        return "finance/user-info";
//    }

    @GetMapping("/user-info")
    public String userInfo(@AuthenticationPrincipal User user, Model model) {
        log.info("사용자 정보 페이지 접근 - userId: {}, email: {}", user.getId(), user.getEmail());

        try {
            // 계좌 목록 조회
            GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
            BankAccountListResponse accounts = bankAccountService.getBankAccounts(accountQuery);
            log.debug("계좌 목록 조회 완료 - 계좌 수: {}, 총 잔액: {}", accounts.getAccounts().size(), accounts.getTotalBalance());

            // 카드 목록 조회
            GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
            CardListResponse cards = cardService.getCards(cardQuery);
            log.debug("카드 목록 조회 완료 - 카드 수: {}", cards.getCards().size());

            model.addAttribute("accounts", accounts.getAccounts());
            model.addAttribute("totalBalance", accounts.getTotalBalance());
            model.addAttribute("cards", cards.getCards());
            model.addAttribute("username", user.getName());
            model.addAttribute("email", user.getEmail());

            log.info("사용자 정보 페이지 렌더링 완료 - userId: {}", user.getId());
            return "finance/user-info";

        } catch (Exception e) {
            log.error("사용자 정보 페이지 로드 실패 - userId: {}, email: {}", user.getId(), user.getEmail(), e);
            model.addAttribute("errorMessage", "정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    @GetMapping("/add-account")
    public String addAccountForm(Model model) {
        model.addAttribute("accountTypes", AccountType.values());
        return "finance/add-account";
    }

    @GetMapping("/add-card")
    public String addCardForm(Model model) {
        model.addAttribute("cardTypes", CardType.values());
        return "finance/add-card";
    }
}
