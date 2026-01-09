package kr.kiomn2.bigtraffic.interfaces.finance.web;

import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.finance.vo.AccountType;
import kr.kiomn2.bigtraffic.domain.finance.vo.CardType;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceWebController {

    private final BankAccountService bankAccountService;
    private final CardService cardService;

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

    @GetMapping("/account/{id}")
    public String accountDetail(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
        log.info("계좌 상세 페이지 접근 - userId: {}, accountId: {}", user.getId(), id);

        try {
            GetBankAccountQuery query = new GetBankAccountQuery(user.getId(), id);
            BankAccountResponse account = bankAccountService.getBankAccount(query);

            model.addAttribute("account", account);
            model.addAttribute("username", user.getName());

            log.info("계좌 상세 페이지 렌더링 완료 - accountId: {}", id);
            return "finance/account-detail";

        } catch (Exception e) {
            log.error("계좌 상세 페이지 로드 실패 - userId: {}, accountId: {}", user.getId(), id, e);
            model.addAttribute("errorMessage", "계좌 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    @GetMapping("/card/{id}")
    public String cardDetail(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
        log.info("카드 상세 페이지 접근 - userId: {}, cardId: {}", user.getId(), id);

        try {
            GetCardQuery query = new GetCardQuery(user.getId(), id);
            CardResponse card = cardService.getCard(query);

            model.addAttribute("card", card);
            model.addAttribute("username", user.getName());

            log.info("카드 상세 페이지 렌더링 완료 - cardId: {}", id);
            return "finance/card-detail";

        } catch (Exception e) {
            log.error("카드 상세 페이지 로드 실패 - userId: {}, cardId: {}", user.getId(), id, e);
            model.addAttribute("errorMessage", "카드 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }
}
