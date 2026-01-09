package kr.kiomn2.bigtraffic.application.accountbook.service;

import kr.kiomn2.bigtraffic.application.accountbook.command.CreateTransactionCommand;
import kr.kiomn2.bigtraffic.application.accountbook.command.DeleteTransactionCommand;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionQuery;
import kr.kiomn2.bigtraffic.application.accountbook.query.GetTransactionsQuery;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBalanceCommand;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetCardQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.application.finance.service.CardService;
import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.PaymentMethod;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.domain.finance.entity.Card;
import kr.kiomn2.bigtraffic.infrastructure.accountbook.repository.CategoryRepository;
import kr.kiomn2.bigtraffic.infrastructure.accountbook.repository.TransactionRepository;
import kr.kiomn2.bigtraffic.infrastructure.finance.repository.BankAccountRepository;
import kr.kiomn2.bigtraffic.infrastructure.finance.repository.CardRepository;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.CardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 거래 서비스
 * 거래 생성 시 계좌/카드 잔액/사용금액을 자동으로 업데이트
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CardRepository cardRepository;
    private final BankAccountService bankAccountService;
    private final CardService cardService;

    /**
     * 거래 생성
     * - ACCOUNT 결제: 계좌 잔액 증감
     * - CARD 결제: 체크카드 잔액 증감 또는 신용카드 사용금액 증가
     * - CASH 결제: 별도 처리 없음
     */
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionCommand command) {
        log.info("거래 생성 시작 - userId: {}, type: {}, amount: {}, paymentMethod: {}",
                command.getUserId(), command.getType(), command.getAmount(), command.getPaymentMethod());

        // 카테고리 검증
        categoryRepository.findByIdAndUserId(command.getCategoryId(), command.getUserId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        // 거래 생성
        Transaction transaction = Transaction.create(
                command.getUserId(), command.getType(), command.getAmount(),
                command.getCategoryId(), command.getDescription(),
                command.getTransactionDate(), command.getPaymentMethod(),
                command.getAccountId(), command.getCardId(), command.getMemo()
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 결제 수단에 따른 잔액/사용금액 업데이트
        updatePaymentMethodBalance(
                command.getUserId(), command.getType(), command.getAmount(),
                command.getPaymentMethod(), command.getAccountId(), command.getCardId()
        );

        log.info("거래 생성 완료 - transactionId: {}", savedTransaction.getId());
        return TransactionResponse.from(savedTransaction);
    }

    /**
     * 결제 수단별 잔액/사용금액 업데이트
     */
    private void updatePaymentMethodBalance(
            Long userId,
            TransactionType type,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            Long accountId,
            Long cardId
    ) {
        switch (paymentMethod) {
            case ACCOUNT -> updateAccountBalance(userId, type, amount, accountId);
            case CARD -> updateCardBalance(userId, type, amount, cardId);
            case CASH -> log.debug("현금 거래 - 별도 잔액 업데이트 없음");
        }
    }

    /**
     * 계좌 잔액 업데이트
     * INCOME: 잔액 증가
     * EXPENSE: 잔액 감소
     */
    private void updateAccountBalance(Long userId, TransactionType type, BigDecimal amount, Long accountId) {
        if (accountId == null) {
            throw new RuntimeException("계좌 결제 시 accountId가 필요합니다.");
        }

        BankAccountResponse account = bankAccountService.getBankAccount(
                new GetBankAccountQuery(userId, accountId)
        );

        BigDecimal newBalance;
        if (type == TransactionType.INCOME) {
            newBalance = account.getBalance().add(amount);
            log.debug("계좌 수입 - accountId: {}, 이전 잔액: {}, 추가 금액: {}, 새 잔액: {}",
                    accountId, account.getBalance(), amount, newBalance);
        } else {
            newBalance = account.getBalance().subtract(amount);
            log.debug("계좌 지출 - accountId: {}, 이전 잔액: {}, 차감 금액: {}, 새 잔액: {}",
                    accountId, account.getBalance(), amount, newBalance);
        }

        bankAccountService.updateBalance(new UpdateBalanceCommand(userId, accountId, newBalance));
    }

    /**
     * 카드 잔액/사용금액 업데이트
     * 체크카드: INCOME(잔액 증가), EXPENSE(잔액 감소)
     * 신용카드: EXPENSE(사용금액 증가)
     */
    private void updateCardBalance(Long userId, TransactionType type, BigDecimal amount, Long cardId) {
        if (cardId == null) {
            throw new RuntimeException("카드 결제 시 cardId가 필요합니다.");
        }

        CardResponse card = cardService.getCard(new GetCardQuery(userId, cardId));

        if ("DEBIT".equals(card.getCardType().toString())) {
            // 체크카드: 잔액 증감
            BigDecimal newBalance;
            if (type == TransactionType.INCOME) {
                newBalance = card.getBalance().add(amount);
                log.debug("체크카드 수입 - cardId: {}, 이전 잔액: {}, 추가 금액: {}, 새 잔액: {}",
                        cardId, card.getBalance(), amount, newBalance);
            } else {
                newBalance = card.getBalance().subtract(amount);
                log.debug("체크카드 지출 - cardId: {}, 이전 잔액: {}, 차감 금액: {}, 새 잔액: {}",
                        cardId, card.getBalance(), amount, newBalance);
            }

            // 카드 잔액 업데이트
            Card cardEntity = cardRepository.findByIdAndUserId(cardId, userId)
                    .orElseThrow(() -> new RuntimeException("카드를 찾을 수 없습니다."));
            cardEntity.updateBalance(newBalance);

        } else if ("CREDIT".equals(card.getCardType().toString())) {
            // 신용카드: EXPENSE 시 사용금액 증가
            if (type == TransactionType.EXPENSE) {
                Card cardEntity = cardRepository.findByIdAndUserId(cardId, userId)
                        .orElseThrow(() -> new RuntimeException("카드를 찾을 수 없습니다."));

                BigDecimal currentUsed = cardEntity.getUsedAmount() != null ? cardEntity.getUsedAmount() : BigDecimal.ZERO;
                BigDecimal newUsedAmount = currentUsed.add(amount);

                log.debug("신용카드 지출 - cardId: {}, 이전 사용금액: {}, 추가 사용: {}, 새 사용금액: {}",
                        cardId, currentUsed, amount, newUsedAmount);

                cardEntity.updateUsedAmount(newUsedAmount);
            } else {
                log.debug("신용카드 수입은 별도 처리하지 않습니다.");
            }
        }
    }

    /**
     * 거래 목록 조회
     */
    public List<TransactionResponse> getTransactions(GetTransactionsQuery query) {
        List<Transaction> transactions = transactionRepository.findByDynamicConditions(
                query.getUserId(), query.getType(), query.getCategoryId(),
                query.getStartDate(), query.getEndDate(),
                query.getAccountId(), query.getCardId()
        );

        return transactions.stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * 거래 상세 조회
     */
    public TransactionResponse getTransaction(GetTransactionQuery query) {
        Transaction transaction = transactionRepository.findByIdAndUserId(
                query.getTransactionId(), query.getUserId()
        ).orElseThrow(() -> new RuntimeException("거래를 찾을 수 없습니다."));

        return TransactionResponse.from(transaction);
    }

    /**
     * 거래 삭제
     */
    @Transactional
    public void deleteTransaction(DeleteTransactionCommand command) {
        Transaction transaction = transactionRepository.findByIdAndUserId(
                command.getTransactionId(), command.getUserId()
        ).orElseThrow(() -> new RuntimeException("거래를 찾을 수 없습니다."));

        // TODO: 거래 삭제 시 계좌/카드 잔액 복원 로직 추가 (선택사항)

        transactionRepository.delete(transaction);
        log.info("거래 삭제 완료 - transactionId: {}", command.getTransactionId());
    }
}
