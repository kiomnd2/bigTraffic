package kr.kiomn2.bigtraffic.domain.finance.service;

import kr.kiomn2.bigtraffic.domain.finance.command.*;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.exception.BankAccountNotFoundException;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.domain.finance.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final FinanceDataEncryptor encryptor;

    @Transactional
    public BankAccount createBankAccount(CreateBankAccountCommand command) {
        String lastFourDigits = encryptor.extractLastFourDigits(command.getAccountNumber());

        BankAccount bankAccount = BankAccount.create(
                command.getAccountNumber(),
                lastFourDigits,
                command
        );

        if (bankAccountRepository.countByUserId(command.getUserId()) == 0) {
            bankAccount.setAsDefault();
        }

        return bankAccountRepository.save(bankAccount);
    }

    public List<BankAccount> getBankAccounts(GetBankAccountsQuery query) {
        return bankAccountRepository.findByDynamicConditions(
                query.getUserId(),
                query.getIsActive()
        );
    }

    public BankAccount getBankAccount(GetBankAccountQuery query) {
        return getBankAccountById(query.getUserId(), query.getAccountId());
    }

    @Transactional
    public BankAccount updateBankAccount(UpdateBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());
        bankAccount.updateFullInfo(command);
        return bankAccount;
    }

    @Transactional
    public void deleteBankAccount(DeleteBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());

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
    public BankAccount setDefaultBankAccount(SetDefaultBankAccountCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());

        bankAccountRepository.unsetDefaultForOtherAccounts(command.getUserId(), command.getAccountId());
        bankAccount.setAsDefault();

        return bankAccount;
    }

    @Transactional
    public BankAccount updateBalance(UpdateBalanceCommand command) {
        BankAccount bankAccount = getBankAccountById(command.getUserId(), command.getAccountId());
        bankAccount.updateBalance(command.getBalance());
        return bankAccount;
    }

    private BankAccount getBankAccountById(Long userId, Long accountId) {
        return bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new BankAccountNotFoundException("계좌를 찾을 수 없습니다."));
    }
}
