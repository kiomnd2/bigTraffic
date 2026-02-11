package kr.kiomn2.bigtraffic.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // JWT
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "블랙리스트에 등록된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Finance
    BANK_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계좌입니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카드입니다."),
    DUPLICATE_FINANCE_ASSET(HttpStatus.BAD_REQUEST, "이미 등록된 금융자산입니다."),

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String message;
}