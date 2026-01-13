package kr.kiomn2.bigtraffic.interfaces.accountbook.api;

import kr.kiomn2.bigtraffic.application.accountbook.command.CreateTransactionCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.DeleteTransactionCommand;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetMonthlyCalendarQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionsPagedQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionsQuery;
import kr.kiomn2.bigtraffic.application.accountbook.service.TransactionService;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.request.TransactionCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.MonthlyCalendarResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
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

/**
 * 가계부 거래 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 거래 생성
     * 결제 수단에 따라 계좌/카드 잔액이 자동으로 업데이트됩니다.
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionCreateRequest request
    ) {
        CreateTransactionCommand command = CreateTransactionCommand.from(user.getId(), request);
        TransactionResponse response = transactionService.createTransaction(command);
        return ResponseEntity.ok(response);
    }

    /**
     * 거래 목록 조회
     * 동적 필터링 지원 (유형, 카테고리, 기간, 계좌, 카드)
     */
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
        List<TransactionResponse> transactions = transactionService.getTransactions(query);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 거래 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        GetTransactionQuery query = new GetTransactionQuery(user.getId(), id);
        TransactionResponse transaction = transactionService.getTransaction(query);
        return ResponseEntity.ok(transaction);
    }

    /**
     * 거래 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        DeleteTransactionCommand command = new DeleteTransactionCommand(user.getId(), id);
        transactionService.deleteTransaction(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * 거래 목록 조회 (페이징 지원)
     * 동적 필터링 및 페이지네이션 지원
     */
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
        Page<TransactionResponse> transactions = transactionService.getTransactionsPaged(query);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 월별 캘린더 데이터 조회
     * 날짜별 거래 요약 및 월별 합계 제공
     */
    @GetMapping("/calendar/{year}/{month}")
    public ResponseEntity<MonthlyCalendarResponse> getMonthlyCalendar(
            @AuthenticationPrincipal User user,
            @PathVariable int year,
            @PathVariable int month
    ) {
        GetMonthlyCalendarQuery query = new GetMonthlyCalendarQuery(user.getId(), year, month);
        MonthlyCalendarResponse calendar = transactionService.getMonthlyCalendar(query);
        return ResponseEntity.ok(calendar);
    }
}
