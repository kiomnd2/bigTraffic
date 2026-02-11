package kr.kiomn2.bigtraffic.interfaces.finance.mapper;

import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.service.FinanceDataEncryptor;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {

    private final FinanceDataEncryptor encryptor;

    public BankAccountResponse toResponse(BankAccount account) {
        String masked = encryptor.maskAccountNumber(account.getAccountNumber());
        return BankAccountResponse.from(account, masked);
    }

    public BankAccountResponse toDetailResponse(BankAccount account) {
        String decrypted = encryptor.decrypt(account.getAccountNumber());
        String masked = encryptor.maskAccountNumber(account.getAccountNumber());
        return BankAccountResponse.fromWithAccountNumber(account, decrypted, masked);
    }

    public BankAccountListResponse toListResponse(List<BankAccount> accounts) {
        List<BankAccountResponse> accountResponses = accounts.stream()
                .map(this::toResponse)
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
}
