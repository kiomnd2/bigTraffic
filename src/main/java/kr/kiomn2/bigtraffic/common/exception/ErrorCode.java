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

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 방입니다."),
    ROOM_ALREADY_DISSOLVED(HttpStatus.BAD_REQUEST, "이미 해산된 방입니다."),
    ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "방 멤버를 찾을 수 없습니다."),
    ALREADY_ROOM_MEMBER(HttpStatus.BAD_REQUEST, "이미 참여 중인 방입니다."),
    ROOM_MEMBER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "방 최대 인원을 초과했습니다."),
    ROOM_HOST_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "방장 권한이 필요합니다."),
    INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 코드입니다."),
    ROOM_ACCOUNT_BOOK_ALREADY_LINKED(HttpStatus.BAD_REQUEST, "이미 연결된 가계부입니다."),
    ROOM_ACCOUNT_BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "연결된 가계부를 찾을 수 없습니다."),
    CANNOT_LEAVE_AS_HOST(HttpStatus.BAD_REQUEST, "방장은 방을 나갈 수 없습니다. 방을 해산하거나 방장을 위임해주세요."),

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String message;
}