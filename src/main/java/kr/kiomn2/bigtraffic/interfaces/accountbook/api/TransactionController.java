package kr.kiomn2.bigtraffic.interfaces.accountbook.api;

import kr.kiomn2.bigtraffic.application.accountbook.facade.AccountBookFacade;
import kr.kiomn2.bigtraffic.domain.accountbook.command.CreateTransactionCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.DeleteTransactionCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.query.*;
import kr.kiomn2.bigtraffic.domain.accountbook.service.TransactionService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.TransactionCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.MonthlyCalendarResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountBookFacade accountBookFacade;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionCreateRequest request
    ) {
        CreateTransactionCommand command = new CreateTransactionCommand(
                user.getId(), request.getType(), request.getAmount(),
                request.getCategoryId(), request.getDescription(),
                request.getTransactionDate(), request.getPaymentMethod(),
                request.getAccountId(), request.getCardId(), request.getMemo()
        );
        Transaction tx = accountBookFacade.createTransactionWithBalance(command);
        return ResponseEntity.ok(transactionMapper.toResponse(tx));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long cardId
    ) {
        GetTransactionsQuery query = new GetTransactionsQuery(
                user.getId(), type, categoryId, startDate, endDate, accountId, cardId
        );
        List<Transaction> transactions = transactionService.getTransactions(query);
        return ResponseEntity.ok(transactionMapper.toResponseList(transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        GetTransactionQuery query = new GetTransactionQuery(user.getId(), id);
        Transaction transaction = transactionService.getTransaction(query);
        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        DeleteTransactionCommand command = new DeleteTransactionCommand(user.getId(), id);
        transactionService.deleteTransaction(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsPaged(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long cardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        GetTransactionsPagedQuery query = new GetTransactionsPagedQuery(
                user.getId(), type, categoryId, startDate, endDate, accountId, cardId, pageable
        );
        Page<Transaction> transactions = transactionService.getTransactionsPaged(query);
        return ResponseEntity.ok(transactionMapper.toResponsePage(transactions));
    }

    @GetMapping("/calendar/{year}/{month}")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @AuthenticationPrincipal User user,
            @PathVariable int year,
            @PathVariable int month
    ) {
        GetMonthlyCalendarQuery query = new GetMonthlyCalendarQuery(user.getId(), year, month);
        Map<LocalDate, List<Transaction>> grouped = transactionService.getMonthlyTransactions(query);
        MonthlyCalendarResponse calendar = transactionMapper.toMonthlyCalendarResponse(year, month, grouped);
        return ResponseEntity.ok(calendar);
    }
}
