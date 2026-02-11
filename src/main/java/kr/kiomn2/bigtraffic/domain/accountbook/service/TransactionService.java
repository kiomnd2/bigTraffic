package kr.kiomn2.bigtraffic.domain.accountbook.service;

import kr.kiomn2.bigtraffic.domain.accountbook.command.CreateTransactionCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.command.DeleteTransactionCommand;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.query.*;
import kr.kiomn2.bigtraffic.domain.accountbook.repository.CategoryRepository;
import kr.kiomn2.bigtraffic.domain.accountbook.repository.TransactionRepository;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Transaction createTransaction(CreateTransactionCommand command) {
        log.info("거래 생성 시작 - userId: {}, type: {}, amount: {}, paymentMethod: {}",
                command.getUserId(), command.getType(), command.getAmount(), command.getPaymentMethod());

        categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Transaction transaction = Transaction.create(command);
        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("거래 생성 완료 - transactionId: {}", savedTransaction.getId());
        return savedTransaction;
    }

    public List<Transaction> getTransactions(GetTransactionsQuery query) {
        return transactionRepository.findByDynamicConditions(
                query.getUserId(), query.getType(), query.getCategoryId(),
                query.getStartDate(), query.getEndDate(),
                query.getAccountId(), query.getCardId()
        );
    }

    public Transaction getTransaction(GetTransactionQuery query) {
        return transactionRepository.findByIdAndUserId(
                query.getTransactionId(), query.getUserId()
        ).orElseThrow(() -> new RuntimeException("거래를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteTransaction(DeleteTransactionCommand command) {
        Transaction transaction = transactionRepository.findByIdAndUserId(
                command.getTransactionId(), command.getUserId()
        ).orElseThrow(() -> new RuntimeException("거래를 찾을 수 없습니다."));

        transactionRepository.delete(transaction);
        log.info("거래 삭제 완료 - transactionId: {}", command.getTransactionId());
    }

    public Page<Transaction> getTransactionsPaged(GetTransactionsPagedQuery query) {
        return transactionRepository.findByDynamicConditionsWithPaging(
                query.getUserId(), query.getType(), query.getCategoryId(),
                query.getStartDate(), query.getEndDate(),
                query.getAccountId(), query.getCardId(),
                query.getPageable()
        );
    }

    public Map<LocalDate, List<Transaction>> getMonthlyTransactions(GetMonthlyCalendarQuery query) {
        LocalDate startDate = LocalDate.of(query.getYear(), query.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        log.debug("월별 캘린더 조회 - userId: {}, year: {}, month: {}, range: {} ~ {}",
                query.getUserId(), query.getYear(), query.getMonth(), startDate, endDate);

        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(
                query.getUserId(), startDate, endDate
        );

        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getTransactionDate));
    }
}
