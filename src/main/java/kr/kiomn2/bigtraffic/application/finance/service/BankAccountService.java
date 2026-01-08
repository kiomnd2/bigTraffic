package kr.kiomn2.bigtraffic.application.finance.service;

import kr.kiomn2.bigtraffic.application.finance.command.CreateBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.DeleteBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.SetDefaultBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBalanceCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.exception.BankAccountNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.finance.repository.BankAccountRepository;
import kr.kiomn2.bigtraffic.infrastructure.finance.security.FinanceDataEncryptor;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final FinanceDataEncryptor encryptor;

    @Transactional
    public BankAccountResponse createBankAccount(CreateBankAccountCommand command) {
        String lastFourDigits = encryptor.extractLastFourDigits(command.getAccountNumber());

        BankAccount bankAccount = BankAccount.create(
                command.getAccountNumber(),
                lastFourDigits,
                command
        );

        // 첫 번째 계좌인 경우 자동으로 기본 계좌로 설정
        if (bankAccountRepository.countByUserId(command.getUserId()) == 0) {
            bankAccount.setAsDefault();
        }

        BankAccount savedAccount = bankAccountRepository.save(bankAccount);
        String maskedAccountNumber = getMaskedAccountNumber(savedAccount);
        return BankAccountResponse.from(savedAccount, maskedAccountNumber);
    }

    public BankAccountListResponse getBankAccounts(GetBankAccountsQuery query) {
        // QueryDSL 동적 쿼리 사용
        List<BankAccount> accounts = bankAccountRepository.findByDynamicConditions(
                query.getUserId(),
                query.getIsActive()
        );

        List<BankAccountResponse> accountResponses = accounts.stream()
                .map(account -> BankAccountResponse.from(
                        account,
                        getMaskedAccountNumber(account)
                ))
                .toList();

        BigDecimal totalBalance = accounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BankAccountListResponse.builder()
                .accounts(accountResponses)
                .totalBalance(totalBalance)
                .totalCount(accountResponses.size())
                .build();
    }

    public BankAccountResponse getBankAccount(GetBankAccountQuery query) {
        BankAccount bankAccount = getBankAccountById(query.getUserId(), query.getAccountId());
        String maskedAccountNumber = getMaskedAccountNumber(bankAccount);
        return BankAccountResponse.from(bankAccount, maskedAccountNumber);
    }

    @Transactional
    public BankAccountResponse updateBankAccount(UpdateBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());
        bankAccount.updateInfo(command.getAccountName(), command.getColor(), command.getMemo());

        String maskedAccountNumber = getMaskedAccountNumber(bankAccount);
        return BankAccountResponse.from(bankAccount, maskedAccountNumber);
    }

    @Transactional
    public void deleteBankAccount(DeleteBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());

        // 기본 계좌를 삭제하는 경우, 다른 계좌를 기본으로 설정
        if (bankAccount.getIsDefault()) {
            List<BankAccount> otherAccounts = bankAccountRepository.findByUserId(command.getUserId()).stream()
                    .filter(account -> !account.getId().equals(command.getAccountId()))
                    .filter(BankAccount::getIsActive)
                    .toList();

            if (!otherAccounts.isEmpty()) {
                otherAccounts.get(0).setAsDefault();
            }
        }

        bankAccountRepository.delete(bankAccount);
    }

    @Transactional
    public BankAccountResponse setDefaultBankAccount(SetDefaultBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());

        // 다른 계좌의 기본 설정 해제
        bankAccountRepository.unsetDefaultForOtherAccounts(command.getUserId(), command.getAccountId());

        // 현재 계좌를 기본으로 설정
        bankAccount.setAsDefault();

        String maskedAccountNumber = getMaskedAccountNumber(bankAccount);
        return BankAccountResponse.from(bankAccount, maskedAccountNumber);
    }

    @Transactional
    public BankAccountResponse updateBalance(UpdateBalanceCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());
        bankAccount.updateBalance(command.getBalance());

        String maskedAccountNumber = getMaskedAccountNumber(bankAccount);
        return BankAccountResponse.from(bankAccount, maskedAccountNumber);
    }

    private BankAccount getBankAccountById(Long userId, Long accountId) {
        return bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new BankAccountNotFoundException("계좌를 찾을 수 없습니다."));
    }

    private String getMaskedAccountNumber(BankAccount bankAccount) {
        return encryptor.maskAccountNumber(bankAccount.getAccountNumber());
    }
}
