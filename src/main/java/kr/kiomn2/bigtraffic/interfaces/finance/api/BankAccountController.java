package kr.kiomn2.bigtraffic.interfaces.finance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kiomn2.bigtraffic.application.finance.command.CreateBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.DeleteBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.SetDefaultBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBalanceCommand;
import kr.kiomn2.bigtraffic.application.finance.command.UpdateBankAccountCommand;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountQuery;
import kr.kiomn2.bigtraffic.application.finance.query.GetBankAccountsQuery;
import kr.kiomn2.bigtraffic.application.finance.service.BankAccountService;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BalanceUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountCreateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.request.BankAccountUpdateRequest;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountListResponse;
import kr.kiomn2.bigtraffic.interfaces.finance.dto.response.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bank Account", description = "계좌 관리 API")
@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Operation(summary = "계좌 등록", description = "새로운 은행 계좌를 등록합니다.")
    @PostMapping
    public ResponseEntity<BankAccountResponse> createBankAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BankAccountCreateRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());

        CreateBankAccountCommand command = CreateBankAccountCommand.from(userId, request);
        BankAccountResponse response = bankAccountService.createBankAccount(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "계좌 목록 조회", description = "사용자의 계좌 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<BankAccountListResponse> getBankAccounts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Boolean isActive) {

        Long userId = Long.parseLong(userDetails.getUsername());

        GetBankAccountsQuery query = new GetBankAccountsQuery(userId, isActive);
        BankAccountListResponse response = bankAccountService.getBankAccounts(query);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "계좌 상세 조회", description = "특정 계좌의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getBankAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        GetBankAccountQuery query = new GetBankAccountQuery(userId, id);
        BankAccountResponse response = bankAccountService.getBankAccount(query);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "계좌 수정", description = "계좌 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponse> updateBankAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody BankAccountUpdateRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());

        UpdateBankAccountCommand command = UpdateBankAccountCommand.from(userId, id, request);
        BankAccountResponse response = bankAccountService.updateBankAccount(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "계좌 삭제", description = "계좌를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        DeleteBankAccountCommand command = new DeleteBankAccountCommand(userId, id);
        bankAccountService.deleteBankAccount(command);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "기본 계좌 설정", description = "특정 계좌를 기본 계좌로 설정합니다.")
    @PatchMapping("/{id}/default")
    public ResponseEntity<BankAccountResponse> setDefaultBankAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        Long userId = Long.parseLong(userDetails.getUsername());

        SetDefaultBankAccountCommand command = new SetDefaultBankAccountCommand(userId, id);
        BankAccountResponse response = bankAccountService.setDefaultBankAccount(command);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "잔액 업데이트", description = "계좌 잔액을 업데이트합니다.")
    @PatchMapping("/{id}/balance")
    public ResponseEntity<BankAccountResponse> updateBalance(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody BalanceUpdateRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());

        UpdateBalanceCommand command = new UpdateBalanceCommand(userId, id, request.getBalance());
        BankAccountResponse response = bankAccountService.updateBalance(command);

        return ResponseEntity.ok(response);
    }
}
