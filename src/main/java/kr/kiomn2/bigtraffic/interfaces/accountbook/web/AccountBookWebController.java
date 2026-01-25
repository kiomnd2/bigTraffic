package kr.kiomn2.bigtraffic.interfaces.accountbook.web;

import kr.kiomn2.bigtraffic.application.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionQuery;
import kr.kiomn2.bigtraffic.application.accountbook.service.CategoryService;
import kr.kiomn2.bigtraffic.application.accountbook.service.TransactionService;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 가계부 웹 컨트롤러
 * 가계부 관련 페이지 렌더링
 */
@Slf4j
@Controller
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class AccountBookWebController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final BankAccountService bankAccountService;
    private final CardService cardService;

    /**
     * 가계부 메인 페이지 (월별 캘린더 + 거래 목록)
     */
    @GetMapping
    public String ledgerMain(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model
    ) {
        log.info("가계부 메인 페이지 접근 - userId: {}", user.getId());

        // 기본값: 현재 연월
        LocalDate now = LocalDate.now();
        int targetYear = year != null ? year : now.getYear();
        int targetMonth = month != null ? month : now.getMonthValue();

        try {
            // 카테고리 목록 조회 (활성화된 카테고리만)
            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<CategoryResponse> categories = categoryService.getCategories(categoryQuery);

            model.addAttribute("username", user.getName());
            model.addAttribute("currentYear", targetYear);
            model.addAttribute("currentMonth", targetMonth);
            model.addAttribute("categories", categories);
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("paymentMethods", PaymentMethod.values());

            log.info("가계부 메인 페이지 렌더링 완료 - userId: {}", user.getId());
            return "ledger/ledger-main";

        } catch (Exception e) {
            log.error("가계부 메인 페이지 로드 실패 - userId: {}", user.getId(), e);
            model.addAttribute("errorMessage", "가계부 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    /**
     * 거래 추가 페이지
     */
    @GetMapping("/add-transaction")
    public String addTransactionForm(@AuthenticationPrincipal User user, Model model) {
        log.info("거래 추가 페이지 접근 - userId: {}", user.getId());

        try {
            // 카테고리 목록
            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<CategoryResponse> categories = categoryService.getCategories(categoryQuery);

            // 계좌 목록
            GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
            BankAccountListResponse accounts = bankAccountService.getBankAccounts(accountQuery);

            // 카드 목록
            GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
            CardListResponse cards = cardService.getCards(cardQuery);

            model.addAttribute("username", user.getName());
            model.addAttribute("categories", categories);
            model.addAttribute("accounts", accounts.getAccounts());
            model.addAttribute("cards", cards.getCards());
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("paymentMethods", PaymentMethod.values());

            return "ledger/add-transaction";

        } catch (Exception e) {
            log.error("거래 추가 페이지 로드 실패 - userId: {}", user.getId(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    /**
     * 거래 상세/수정 페이지
     */
    @GetMapping("/transaction/{id}")
    public String transactionDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            Model model
    ) {
        log.info("거래 상세 페이지 접근 - userId: {}, transactionId: {}", user.getId(), id);

        try {
            GetTransactionQuery query = new GetTransactionQuery(user.getId(), id);
            TransactionResponse transaction = transactionService.getTransaction(query);

            // 카테고리 목록
            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<CategoryResponse> categories = categoryService.getCategories(categoryQuery);

            // 계좌 목록
            GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
            BankAccountListResponse accounts = bankAccountService.getBankAccounts(accountQuery);

            // 카드 목록
            GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
            CardListResponse cards = cardService.getCards(cardQuery);

            model.addAttribute("transaction", transaction);
            model.addAttribute("username", user.getName());
            model.addAttribute("categories", categories);
            model.addAttribute("accounts", accounts.getAccounts());
            model.addAttribute("cards", cards.getCards());
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("paymentMethods", PaymentMethod.values());

            return "ledger/transaction-detail";

        } catch (Exception e) {
            log.error("거래 상세 페이지 로드 실패 - userId: {}, transactionId: {}", user.getId(), id, e);
            model.addAttribute("errorMessage", "거래 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    /**
     * 카테고리 관리 페이지
     */
    @GetMapping("/categories")
    public String categoryMain(@AuthenticationPrincipal User user, Model model) {
        log.info("카테고리 관리 페이지 접근 - userId: {}", user.getId());

        model.addAttribute("username", user.getName());
        model.addAttribute("transactionTypes", TransactionType.values());

        return "ledger/category-main";
    }
}
