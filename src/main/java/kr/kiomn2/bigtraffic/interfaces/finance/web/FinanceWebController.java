package kr.kiomn2.bigtraffic.interfaces.finance.web;

import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/user-info")
    public String userInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Long userId = Long.parseLong(userDetails.getUsername());

        // 계좌 목록 조회
        GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(userId, true);
        BankAccountListResponse accounts = bankAccountService.getBankAccounts(accountQuery);

        // 카드 목록 조회
        GetCardsQuery cardQuery = new GetCardsQuery(userId, null, true);
        CardListResponse cards = cardService.getCards(cardQuery);

        model.addAttribute("accounts", accounts.getAccounts());
        model.addAttribute("totalBalance", accounts.getTotalBalance());
        model.addAttribute("cards", cards.getCards());
        model.addAttribute("username", userDetails.getUsername());

        return "finance/user-info";
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
