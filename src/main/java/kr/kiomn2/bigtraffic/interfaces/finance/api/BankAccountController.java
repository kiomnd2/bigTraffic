package kr.kiomn2.bigtraffic.interfaces.finance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kiomn2.bigtraffic.domain.finance.command.*;
import kr.kiomn2.bigtraffic.domain.finance.entity.BankAccount;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.domain.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.domain.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BalanceUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.mapper.BankAccountMapper;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bank Account", description = "계좌 관리 API")
@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;

    @Operation(summary = "계좌 등록", description = "새로운 은행 계좌를 등록합니다.")
    @PostMapping
    public ResponseEntity<BankAccountResponse> createBankAccount(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BankAccountCreateRequest request) {

        Long userId = user.getId();

        CreateBankAccountCommand command = new CreateBankAccountCommand(
                userId, request.getAccountName(), request.getBankName(),
                request.getAccountNumber(), request.getAccountType(),
                request.getBalance(), request.getColor(), request.getMemo()
        );
        BankAccount account = bankAccountService.createBankAccount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountMapper.toResponse(account));
    }

    @Operation(summary = "계좌 목록 조회", description = "사용자의 계좌 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<BankAccountListResponse> getBankAccounts(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Boolean isActive) {

        Long userId = user.getId();

        GetBankAccountsQuery query = new GetBankAccountsQuery(userId, isActive);
        List<BankAccount> accounts = bankAccountService.getBankAccounts(query);

        return ResponseEntity.ok(bankAccountMapper.toListResponse(accounts));
    }

    @Operation(summary = "계좌 상세 조회", description = "특정 계좌의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        GetBankAccountQuery query = new GetBankAccountQuery(userId, id);
        BankAccount account = bankAccountService.getBankAccount(query);

        return ResponseEntity.ok(bankAccountMapper.toDetailResponse(account));
    }

    @Operation(summary = "계좌 수정", description = "계좌 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponse> updateBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody BankAccountUpdateRequest request) {

        Long userId = user.getId();

        UpdateBankAccountCommand command = new UpdateBankAccountCommand(
                userId, id, request.getAccountName(), request.getBankName(),
                request.getAccountType(), request.getBalance(),
                request.getIsActive(), request.getColor(), request.getMemo()
        );
        BankAccount account = bankAccountService.updateBankAccount(command);
        return ResponseEntity.ok(bankAccountMapper.toResponse(account));
    }

    @Operation(summary = "계좌 삭제", description = "계좌를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        DeleteBankAccountCommand command = new DeleteBankAccountCommand(userId, id);
        bankAccountService.deleteBankAccount(command);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "기본 계좌 설정", description = "특정 계좌를 기본 계좌로 설정합니다.")
    @PatchMapping("/{id}/default")
    public ResponseEntity<BankAccountResponse> setDefaultBankAccount(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {

        Long userId = user.getId();

        SetDefaultBankAccountCommand command = new SetDefaultBankAccountCommand(userId, id);
        BankAccount account = bankAccountService.setDefaultBankAccount(command);

        return ResponseEntity.ok(bankAccountMapper.toResponse(account));
    }

    @Operation(summary = "잔액 업데이트", description = "계좌 잔액을 업데이트합니다.")
    @PatchMapping("/{id}/balance")
    public ResponseEntity<BankAccountResponse> updateBalance(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody BalanceUpdateRequest request) {

        Long userId = user.getId();

        UpdateBalanceCommand command = new UpdateBalanceCommand(userId, id, request.getBalance());
        BankAccount account = bankAccountService.updateBalance(command);

        return ResponseEntity.ok(bankAccountMapper.toResponse(account));
    }
}
