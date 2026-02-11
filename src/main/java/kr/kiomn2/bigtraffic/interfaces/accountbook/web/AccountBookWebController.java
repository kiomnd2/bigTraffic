package kr.kiomn2.bigtraffic.interfaces.accountbook.web;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Category;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetCategoriesQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.query.GetTransactionQuery;
import kr.kiomn2.bigtraffic.domain.accountbook.service.CategoryService;
import kr.kiomn2.bigtraffic.domain.accountbook.service.TransactionService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetCardsQuery;
import kr.kiomn2.bigtraffic.domain.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.domain.finance.service.CardService;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.CategoryResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.mapper.CategoryMapper;
import kr.kiomn2.bigtraffic.interfaces.accountbook.mapper.TransactionMapper;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.BankAccountMapper;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.CardMapper;
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

@Slf4j
@Controller
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class AccountBookWebController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final BankAccountService bankAccountService;
    private final CardService cardService;
    private final CategoryMapper categoryMapper;
    private final TransactionMapper transactionMapper;
    private final BankAccountMapper bankAccountMapper;
    private final CardMapper cardMapper;

    @GetMapping
    public String ledgerMain(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model
    ) {
        log.info("가계부 메인 페이지 접근 - userId: {}", user.getId());

        LocalDate now = LocalDate.now();
        int targetYear = year != null ? year : now.getYear();
        int targetMonth = month != null ? month : now.getMonthValue();

        try {
            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<Category> categories = categoryService.getCategories(categoryQuery);

            model.addAttribute("username", user.getName());
            model.addAttribute("currentYear", targetYear);
            model.addAttribute("currentMonth", targetMonth);
            model.addAttribute("categories", categoryMapper.toResponseList(categories));
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

    @GetMapping("/add-transaction")
    public String addTransactionForm(@AuthenticationPrincipal User user, Model model) {
        log.info("거래 추가 페이지 접근 - userId: {}", user.getId());

        try {
            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<Category> categories = categoryService.getCategories(categoryQuery);

            GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
            List<BankAccount> accounts = bankAccountService.getBankAccounts(accountQuery);

            GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
            List<Card> cards = cardService.getCards(cardQuery);

            model.addAttribute("username", user.getName());
            model.addAttribute("categories", categoryMapper.toResponseList(categories));
            model.addAttribute("accounts", bankAccountMapper.toListResponse(accounts).getAccounts());
            model.addAttribute("cards", cardMapper.toListResponse(cards).getCards());
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("paymentMethods", PaymentMethod.values());

            return "ledger/add-transaction";

        } catch (Exception e) {
            log.error("거래 추가 페이지 로드 실패 - userId: {}", user.getId(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    @GetMapping("/transaction/{id}")
    public String transactionDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            Model model
    ) {
        log.info("거래 상세 페이지 접근 - userId: {}, transactionId: {}", user.getId(), id);

        try {
            GetTransactionQuery query = new GetTransactionQuery(user.getId(), id);
            Transaction transaction = transactionService.getTransaction(query);

            GetCategoriesQuery categoryQuery = new GetCategoriesQuery(user.getId(), null, true);
            List<Category> categories = categoryService.getCategories(categoryQuery);

            GetBankAccountsQuery accountQuery = new GetBankAccountsQuery(user.getId(), true);
            List<BankAccount> accounts = bankAccountService.getBankAccounts(accountQuery);

            GetCardsQuery cardQuery = new GetCardsQuery(user.getId(), null, true);
            List<Card> cards = cardService.getCards(cardQuery);

            model.addAttribute("transaction", transactionMapper.toResponse(transaction));
            model.addAttribute("username", user.getName());
            model.addAttribute("categories", categoryMapper.toResponseList(categories));
            model.addAttribute("accounts", bankAccountMapper.toListResponse(accounts).getAccounts());
            model.addAttribute("cards", cardMapper.toListResponse(cards).getCards());
            model.addAttribute("transactionTypes", TransactionType.values());
            model.addAttribute("paymentMethods", PaymentMethod.values());

            return "ledger/transaction-detail";

        } catch (Exception e) {
            log.error("거래 상세 페이지 로드 실패 - userId: {}, transactionId: {}", user.getId(), id, e);
            model.addAttribute("errorMessage", "거래 정보를 불러오는데 실패했습니다.");
            return "error";
        }
    }

    @GetMapping("/categories")
    public String categoryMain(@AuthenticationPrincipal User user, Model model) {
        log.info("카테고리 관리 페이지 접근 - userId: {}", user.getId());

        model.addAttribute("username", user.getName());
        model.addAttribute("transactionTypes", TransactionType.values());

        return "ledger/category-main";
    }
}
